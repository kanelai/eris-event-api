package com.kanelai.eris.eventapi.interfaces.httpserver.api

import com.kanelai.eris.eventapi.interfaces.erisclient.api.ContractFunctionWrapper
import com.kanelai.eris.eventapi.interfaces.erisclient.api.ErisApiClient
import com.kanelai.eris.eventapi.interfaces.erisclient.dto.ErisApi
import com.kanelai.eris.eventapi.interfaces.httpserver.api.ApiServer.logger
import com.kanelai.eris.eventapi.interfaces.httpserver.api.ApiServer.txLogger
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
            logger.info { "Server enqueue API invoked" }

            val topic = call.parameters["topic"]!!
            val timestamp = Instant.now()
            val payload = call.receiveText()
            logger.debug { "Enqueue to blockchain: $payload" }

            // enqueue
            val data = ErisApiClient.gson.toJson(ErisApi.QueueMessageDataDto(timestamp = timestamp, payload = payload))
            ContractFunctionWrapper.enqueue(topic, data)
            logger.debug { "Enqueue to blockchain (topic: $topic): $data" }
            val enqueueResponse = HttpApi.EnqueueResponseDto("ok", HttpApi.EnqueueResponseDataDto(timestamp))
            logger.debug { "HTTP enqueueResponse: $enqueueResponse" }
            call.respond(HttpStatusCode.OK, enqueueResponse)

            txLogger.info { "[PUB] ENQUEUE: topic=[$topic] data=[$data]" }
        }
    }
}
