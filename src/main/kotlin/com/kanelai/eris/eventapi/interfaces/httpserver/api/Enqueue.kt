package com.kanelai.eris.eventapi.interfaces.httpserver.api

import com.kanelai.eris.eventapi.interfaces.erisclient.api.ContractFunctionWrapper
import com.kanelai.eris.eventapi.interfaces.erisclient.api.ErisApiClient
import com.kanelai.eris.eventapi.interfaces.erisclient.dto.ErisApi
import com.kanelai.eris.eventapi.interfaces.httpserver.api.ApiServer.logger
import com.kanelai.eris.eventapi.interfaces.httpserver.dto.HttpApi
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import java.time.Instant

fun Route.enqueue() {
    route("/event-queue/{topic}/enqueue") {
        post {
            logger.info("Server enqueue API invoked")

            val topic = call.parameters["topic"]!!
            val timestamp = Instant.now()
            val payload = call.receiveText()
            logger.debug { "Enqueue payload to blockchain event queue: $payload" }

            // enqueue
            val data = ErisApiClient.gson.toJson(ErisApi.QueueMessageDataDto(timestamp = timestamp, payload = payload))
            ContractFunctionWrapper.enqueue(topic, data)
            call.respond(HttpStatusCode.OK, HttpApi.EnqueueResponseDto("ok", HttpApi.EnqueueResponseDataDto(timestamp)))
        }
    }
}