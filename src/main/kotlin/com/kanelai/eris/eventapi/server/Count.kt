package com.kanelai.eris.eventapi.server

import com.kanelai.eris.eventapi.db.QueueTable
import com.kanelai.eris.eventapi.server.ServerApplication.log
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import org.jetbrains.exposed.sql.Slf4jSqlLogger
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.count() {
    get("/event-queue/count") {
        log.info("Server count API invoked")

        // read from DB
        val countResponse = transaction {
            logger.addLogger(Slf4jSqlLogger)

            val count = QueueTable.selectAll().count()
            CountResponse("ok", CountResponseData(count))
        }

        call.respond(countResponse)
    }
}
