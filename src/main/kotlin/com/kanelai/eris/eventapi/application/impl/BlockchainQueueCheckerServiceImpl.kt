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
    private val txLogger = KotlinLogging.logger("TX")

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
                    logger.trace { "Current block number: $thisBlockNumber" }
                } catch (ex: Exception) {
                    logger.error("Failed to get block number: ${ex.message}", ex)
                }
            }
            prevBlockNumber = thisBlockNumber
            logger.debug { "Block number changed to: $thisBlockNumber" }
            try {
                checkQueue()
            } catch (ex: Exception) {
                logger.error("Failed to poll for queued events: ${ex.message}", ex)
            }
        }
    }

    private fun checkQueue() {
        // queue item count
        val queueItemCount = getCount(topic)
        logger.debug { "Queue item count: $queueItemCount" }

        if (queueItemCount > 0) {
            txLogger.info { "[SUB] COUNT: count=[$queueItemCount]" }
            for (offset in 0 until queueItemCount) {
                // peek
                val peekResponse = peek(topic, offset)
                txLogger.info { "[SUB] PEEK: resp=[$peekResponse]" }

                // write to DB
                val queueMessage = ErisApiClient.gson.fromJson(peekResponse.data, ErisApi.QueueMessageDataDto::class.java)
                txLogger.info { "[SUB] QUEUE_MSG: msg=[$queueMessage]" }
                transaction {
                    addLogger(Slf4jSqlDebugLogger)

                    val rowId = QueueTable.insert {
                        it[sender] = peekResponse.sender
                        it[timestamp] = Instant(queueMessage.timestamp.toEpochMilli()).toDateTime()
                        it[blockNumber] = peekResponse.blockNumber
                        it[payload] = queueMessage.payload
                    } get QueueTable.id
                    txLogger.info { "[SUB] INSERT_DB: offset=[$offset] rowId=[$rowId]" }
                    logger.debug { "Inserted item offset $offset to DB queue table resulting with ID: $rowId" }
                }
            }

            // removeFirstItems
            removeFirstItems(topic, queueItemCount)
            txLogger.info { "[SUB] REMOVE_FIRST_ITEMS: count=[$queueItemCount]" }
            logger.debug { "Removed first $queueItemCount items from blockchain event queue" }
        }
    }

}
