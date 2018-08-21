package com.kanelai.eris.eventapi.server

import com.kanelai.eris.eventapi.AppConfig
import com.kanelai.eris.eventapi.client.EnqueueResponse
import com.kanelai.eris.eventapi.client.QueueData
import com.kanelai.eris.eventapi.client.SmartContractApiClient.apiClient
import com.kanelai.eris.eventapi.server.ServerApplication.log
import io.ktor.application.call
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import kotlinx.coroutines.experimental.runBlocking
import java.net.URL
import java.time.Instant

fun Route.enqueue() {
    post("/event-queue/{topic}/enqueue") {
        log.info("Server enqueue API invoked")

        val topic = call.parameters["topic"]
        val timestamp = Instant.now()
        val payload = call.receiveText()
        log.debug("Enqueue payload to blockchain event queue: $payload")

        runBlocking {
            // enqueue
            val enqueueResponse = apiClient.post<EnqueueResponse> {
                url(URL("${AppConfig.smartContractApiUrl}/$topic/enqueue"))
                contentType(ContentType.Application.Json)
                body = QueueData(timestamp = timestamp, payload = payload)
            }
            log.debug("Blockchain event queue enqueue response: $enqueueResponse")
        }
        call.respond(EnqueueResponse("ok", EnqueueResponseData(timestamp)))
    }
}
