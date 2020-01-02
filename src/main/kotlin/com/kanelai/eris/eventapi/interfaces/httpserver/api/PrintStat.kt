package com.kanelai.eris.eventapi.interfaces.httpserver.api

import com.kanelai.eris.eventapi.domain.model.QueueTable
import com.kanelai.eris.eventapi.interfaces.erisclient.api.ContractFunctionWrapper
import com.kanelai.eris.eventapi.interfaces.httpserver.api.ApiServer.logger
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import org.jetbrains.exposed.sql.selectAll

// TODO: test me
fun Route.printStat() {
    route("/event-queue/print-stat") {
        get {
            logger.info("Server print stat API invoked")

            // read from DB
            val tableRowCount = QueueTable.selectAll().count()

            logger.info { "===== Stat =====" }
            logger.info { "DB queue table row count: $tableRowCount" }
            logger.info { "Block number: ${ContractFunctionWrapper.getBlockNumber()}" }

            call.respond(HttpStatusCode.OK)
        }
    }
}
