package com.kanelai.eris.eventapi.client

import com.kanelai.eris.eventapi.AppConfig
import com.kanelai.eris.eventapi.client.SmartContractApiClient.apiClient
import com.kanelai.eris.eventapi.db.QueueTable
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.experimental.runBlocking
import org.jetbrains.exposed.sql.Slf4jSqlLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.Instant
import org.slf4j.LoggerFactory
import java.net.URL

object BlockchainQueueChecker {

    private val log = LoggerFactory.getLogger(this.javaClass)

    private val apiUrl = AppConfig.smartContractApiUrl
    private val topic = AppConfig.subscribeTopic

    fun startLoop() {
        while (true) {
            // align polling with block generation period
            // i.e. poll for event as soon as a new block is generated
            val firstBlockNumber = getBlockNumber()
            var currentBlockNumber = firstBlockNumber
            while (currentBlockNumber == firstBlockNumber) {
                Thread.sleep(AppConfig.subscribeCheckBlockNumberInterval)
                currentBlockNumber = getBlockNumber()
            }
            log.info("blockNumber: $currentBlockNumber")
            checkQueue()
        }
    }

    private fun getBlockNumber(): Int {
        return runBlocking {
            try {
                val blockNumberResponse = apiClient.get<BlockNumberResponse> {
                    url(URL("${AppConfig.smartContractApiUrl}/blockNumber"))
                }
                log.debug("Blockchain block number: $blockNumberResponse")
                blockNumberResponse.blockNumberResponse!!.blockNumber
            } catch (ex: Exception) {
                -1
            }
        }
    }

    private fun checkQueue() {
        try {
            runBlocking {
                // count
                val countResponse = apiClient.get<CountResponse> {
                    url(URL("$apiUrl/$topic/count"))
                }
                log.debug("Blockchain event queue count: $countResponse")
                val queueItemCount = countResponse.countResponse!!.count

                if (queueItemCount > 0) {
                    for (offset in 0 until queueItemCount) {
                        // peek
                        val peekResponse = apiClient.get<PeekResponse> {
                            url(URL("$apiUrl/$topic/peek/$offset"))
                        }
                        log.debug("Peek blockchain event queue item $offset: $peekResponse")

                        // write to DB
                        val queueMessage = peekResponse.peekResponse!!.item
                        transaction {
                            logger.addLogger(Slf4jSqlLogger)

                            val rowId = QueueTable.insert {
                                it[sender] = queueMessage.sender
                                it[timestamp] = Instant(queueMessage.data.timestamp.toEpochMilli()).toDateTime()
                                it[payload] = queueMessage.data.payload
                            } get QueueTable.id
                            log.debug("Inserted item $offset to DB queue table resulting with ID: $rowId")
                        }
                    }

                    // removeFirstItems
                    val removeFirstItemsResponse = apiClient.post<RemoveFirstItemsResponse> {
                        url(URL("$apiUrl/$topic/removeFirstItems/$queueItemCount"))
                        contentType(ContentType.Application.Json)
                    }
                    log.debug("Remove first $queueItemCount items from blockchain event queue: $removeFirstItemsResponse")
                }
            }
        } catch (ex: Exception) {
            log.error("Error occurred while polling for queued events", ex)
        }
    }

}
