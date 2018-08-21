package com.kanelai.eris.eventapi.server

import com.kanelai.eris.eventapi.db.QueueTable
import com.kanelai.eris.eventapi.server.ServerApplication.log
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import org.jetbrains.exposed.sql.Slf4jSqlLogger
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

fun Route.dequeue() {
    post("/event-queue/dequeue") {
        log.info("Server dequeue API invoked")

        var dequeueResponse = DequeueResponse(returnCode = "queueEmpty")

        // dequeue from DB
        transaction {
            logger.addLogger(Slf4jSqlLogger)

            ReentrantLock().withLock {
                val queueMessages = QueueTable.selectAll().orderBy(QueueTable.timestamp).limit(1)
                if (!queueMessages.empty()) {
                    val queueMessage = queueMessages.first()
                    val dequeueResponseData = DequeueResponseData(
                            sender = queueMessage[QueueTable.sender],
                            timestamp = Instant.ofEpochMilli(queueMessage[QueueTable.timestamp].millis),
                            payload = queueMessage[QueueTable.payload])
                    log.debug("Returning first item from DB queue table with ID ${queueMessage[QueueTable.id]}: $dequeueResponseData")
                    dequeueResponse = DequeueResponse("ok", dequeueResponseData)

                    QueueTable.deleteWhere { QueueTable.id eq queueMessage[QueueTable.id] }
                    log.debug("DB queue table size: ${QueueTable.selectAll().count()}")
                }
            }
        }

        call.respond(dequeueResponse)
    }
}
