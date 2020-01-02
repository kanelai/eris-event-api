package com.kanelai.eris.eventapi.interfaces.httpserver.api

import com.kanelai.eris.eventapi.domain.model.QueueTable
import com.kanelai.eris.eventapi.interfaces.httpserver.api.ApiServer.logger
import com.kanelai.eris.eventapi.interfaces.httpserver.api.ApiServer.txLogger
import com.kanelai.eris.eventapi.interfaces.httpserver.dto.HttpApi
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

fun Route.dequeue() {
    route("/event-queue/dequeue") {
        post {
            logger.info { "Server dequeue API invoked" }

            var dequeueResponse = HttpApi.DequeueResponseDto(returnCode = "queueEmpty")

            // dequeue from DB
            transaction {
                addLogger(Slf4jSqlDebugLogger)

                ReentrantLock().withLock {
                    val queueMessages = QueueTable.selectAll().orderBy(QueueTable.timestamp).limit(1)
                    if (!queueMessages.empty()) {
                        val queueMessage = queueMessages.first()
                        val dequeueResponseData = HttpApi.DequeueResponseDataDto(
                                sender = queueMessage[QueueTable.sender],
                                timestamp = Instant.ofEpochMilli(queueMessage[QueueTable.timestamp].millis),
                                blockNumber = queueMessage[QueueTable.blockNumber],
                                payload = queueMessage[QueueTable.payload])
                        logger.debug { "Returning first item from DB queue table with ID ${queueMessage[QueueTable.id]}: $dequeueResponseData" }
                        dequeueResponse = HttpApi.DequeueResponseDto("ok", dequeueResponseData)

                        QueueTable.deleteWhere { QueueTable.id eq queueMessage[QueueTable.id] }
                        logger.debug { "DB queue table size: ${QueueTable.selectAll().count()}" }

                        txLogger.info { "[SUB] DEQUEUE: id=[${queueMessage[QueueTable.id]}] data=[$dequeueResponseData]" }
                    }
                }
            }

            call.respond(HttpStatusCode.OK, dequeueResponse)
        }
    }
}
