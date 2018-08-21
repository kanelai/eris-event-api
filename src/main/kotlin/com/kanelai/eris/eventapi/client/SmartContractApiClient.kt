package com.kanelai.eris.eventapi.client

import com.kanelai.eris.eventapi.AppConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.engine.config
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature

object SmartContractApiClient {
    val apiClient = getSmartContractApiClient(AppConfig.smartContractApiRequestTimeout.toInt())

    private fun getSmartContractApiClient(requestTimeout: Int) = HttpClient(Apache.config {
        // For timeouts: 0 means infinite, while negative value mean to use the system's default value
        socketTimeout = requestTimeout  // Max time between TCP packets - default 10 seconds
        connectTimeout = 10_000 // Max time to establish an HTTP connection - default 10 seconds
        connectionRequestTimeout = 20_000 // Max time for the connection manager to start a request - 20 seconds

        customizeClient {
            setMaxConnTotal(1000) // Maximum number of socket connections.
            setMaxConnPerRoute(100) // Maximum number of requests for a specific endpoint route.
        }
        customizeRequest {
            // Apache's RequestConfig.Builder
        }
    }) {
        install(JsonFeature) {
            serializer = GsonSerializer {
                // Configurable .GsonBuilder
                serializeNulls()
                disableHtmlEscaping()
            }
        }
    }
}
