package com.kanelai.eris.eventapi.server

import com.codahale.metrics.Slf4jReporter
import com.kanelai.eris.eventapi.AppConfig
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.metrics.Metrics
import io.ktor.routing.Routing
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

object ServerApplication {

    val log = LoggerFactory.getLogger(this.javaClass)!!

    fun Application.module() {
        val metricsLog = LoggerFactory.getLogger("METRICS")

        install(DefaultHeaders)
        install(CallLogging)
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        install(Metrics) {
            Slf4jReporter.forRegistry(registry)
                    .outputTo(metricsLog)
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .build()
                    .start(10, TimeUnit.SECONDS)
        }
        install(Routing) {
            // pub
            enqueue()

            // sub
            if (AppConfig.subscribeEnabled) {
                count()
                dequeue()
            }
        }
    }

}
