package com.kanelai.eris.eventapi.interfaces.httpserver.api

import com.kanelai.eris.eventapi.domain.model.QueueTable
import com.kanelai.eris.eventapi.interfaces.httpserver.api.ApiServer.logger
import com.kanelai.eris.eventapi.interfaces.httpserver.api.ApiServer.txLogger
import com.kanelai.eris.eventapi.interfaces.httpserver.dto.HttpApi
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.count() {
    route("/event-queue/count") {
        get {
            logger.info { "Server count API invoked" }

            // read from DB
            val countResponse = transaction {
                addLogger(Slf4jSqlDebugLogger)

                val count = QueueTable.selectAll().count()
                logger.debug { "Server count API result: $count" }
                HttpApi.CountResponseDto("ok", HttpApi.CountResponseDataDto(count))

                txLogger.info { "[SUB] COUNT: count=[$count]" }
            }

            call.respond(HttpStatusCode.OK, countResponse)
        }
    }
}
