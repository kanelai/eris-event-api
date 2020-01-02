package com.kanelai.eris.eventapi.interfaces.httpserver.api

import com.codahale.metrics.Slf4jReporter
import com.kanelai.eris.eventapi.config.AppConfig
import com.kanelai.eris.eventapi.interfaces.httpserver.dto.HttpApi
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.metrics.dropwizard.DropwizardMetrics
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import mu.KotlinLogging
import org.slf4j.event.Level
import java.util.concurrent.TimeUnit

object ApiServer {

    val logger = KotlinLogging.logger {}

    fun Application.module() {
        val metricsLog = KotlinLogging.logger("METRICS")

        install(DefaultHeaders)
        install(CallLogging) {
            level = Level.INFO
        }
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        install(CORS) {
            method(HttpMethod.Options)
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Put)
            method(HttpMethod.Delete)
            method(HttpMethod.Patch)
            header(HttpHeaders.Authorization)
            allowCredentials = true
            anyHost()
        }
        install(DropwizardMetrics) {
            Slf4jReporter.forRegistry(registry)
                    .outputTo(metricsLog)
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .build()
                    .start(10, TimeUnit.SECONDS)
        }
        install(StatusPages) {
            exception<Exception> { ex ->
                call.respond(HttpStatusCode.InternalServerError, HttpApi.ExceptionResponseDto(ex.message!!))
            }
        }
        install(Routing) {
            // pub
            enqueue()

            // sub
            if (AppConfig.subscribeEnabled) {
                count()
                dequeue()
                printStat()
            }

            get("/*") {
                throw Exception("Invalid URL")
            }
        }
    }

}
