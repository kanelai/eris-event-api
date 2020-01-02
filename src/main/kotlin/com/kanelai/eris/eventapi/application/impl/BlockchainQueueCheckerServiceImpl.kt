package com.kanelai.eris.eventapi.application.impl

import com.kanelai.eris.eventapi.config.AppConfig
import com.kanelai.eris.eventapi.domain.model.QueueTable
import com.kanelai.eris.eventapi.interfaces.erisclient.api.ContractFunctionWrapper.getBlockNumber
import com.kanelai.eris.eventapi.interfaces.erisclient.api.ContractFunctionWrapper.getCount
import com.kanelai.eris.eventapi.interfaces.erisclient.api.ContractFunctionWrapper.peek
import com.kanelai.eris.eventapi.interfaces.erisclient.api.ContractFunctionWrapper.removeFirstItems
import com.kanelai.eris.eventapi.interfaces.erisclient.api.ErisApiClient
import com.kanelai.eris.eventapi.interfaces.erisclient.dto.ErisApi
import mu.KotlinLogging
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.Instant

object BlockchainQueueCheckerServiceImpl {

    private val logger = KotlinLogging.logger {}

    private val topic = AppConfig.subscribeTopic

    fun startLoop() {
        var prevBlockNumber = getBlockNumber()
        var thisBlockNumber = prevBlockNumber
        while (true) {
            // align polling with block generation period
            // i.e. poll for event as soon as a new block is generated
            while (thisBlockNumber == prevBlockNumber) {
                Thread.sleep(AppConfig.subscribeCheckBlockNumberInterval)
                try {
                    thisBlockNumber = getBlockNumber()
                } catch (ex: Exception) {
                    logger.error("Failed to get block number: ${ex.message}", ex)
                }
            }
            prevBlockNumber = thisBlockNumber
            logger.info("blockNumber: $thisBlockNumber")
            try {
                checkQueue()
            } catch (ex: Exception) {
                logger.error("Failed to poll for queued events: ${ex.message}", ex)
            }
        }
    }

    private fun checkQueue() {
        // count
        val queueItemCount = getCount(topic)

        if (queueItemCount > 0) {
            for (offset in 0 until queueItemCount) {
                // peek
                val peekResponse = peek(topic, offset)

                // write to DB
                val queueMessage = ErisApiClient.gson.fromJson(peekResponse.data, ErisApi.QueueMessageDataDto::class.java)
                transaction {
                    addLogger(Slf4jSqlDebugLogger)

                    val rowId = QueueTable.insert {
                        it[sender] = peekResponse.sender
                        it[timestamp] = Instant(queueMessage.timestamp.toEpochMilli()).toDateTime()
                        it[blockNumber] = peekResponse.blockNumber
                        it[payload] = queueMessage.payload
                    } get QueueTable.id
                    logger.debug { "Inserted item $offset to DB queue table resulting with ID: $rowId" }
                }
            }

            // removeFirstItems
            removeFirstItems(topic, queueItemCount)
            logger.debug { "Removed first $queueItemCount items from blockchain event queue" }
        }
    }

}
