package com.kanelai.eris.eventapi.interfaces.erisclient.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.kanelai.eris.eventapi.config.AppConfig
import com.kanelai.eris.eventapi.interfaces.erisclient.dto.ErisApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.engine.config
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.TrustStrategy
import org.apache.http.ssl.SSLContexts
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import java.io.File
import java.io.FileInputStream
import java.net.URL
import java.security.KeyStore

object ErisApiClient {

    private val logger = KotlinLogging.logger {}

    private val apiClient = getErisApiHttpClient(AppConfig.erisApiRequestTimeout.toInt())
    val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()!!

    private fun getErisApiHttpClient(requestTimeout: Int) = HttpClient(Apache.config {
        // For timeouts: 0 means infinite, while negative value mean to use the system's default value
        socketTimeout = requestTimeout  // Max time between TCP packets - default 10 seconds
        connectTimeout = 10_000 // Max time to establish an HTTP connection - default 10 seconds
        connectionRequestTimeout = 20_000 // Max time for the connection manager to start a request - 20 seconds

        // accept all
        val acceptingTrustStrategy = TrustStrategy { _, _ -> true }

        // load key store
        /*
        present client cert signed by the server CA cert's private key for server to verify
        Eris "CA" cert's private key -sign-> "local_node" cert (for server-side Eris node port 1337)
        Eris "CA" cert's private key -sign-> "eris-api-client-cert-key-store" cert (for this client-side)
         */
        logger.debug { "Using client cert in keystore ${AppConfig.keystore}" }
        val keystore = File(AppConfig.keystore)
        val keystorePassword = AppConfig.keystorePassword.toCharArray()
        val keyPassword = AppConfig.keyPassword.toCharArray()

        val mySslContext = if (AppConfig.trustAllServerCert) {
            // trust all server cert
            logger.debug { "Trusting all server cert" }

            SSLContexts
                    .custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .loadKeyMaterial(keystore, keystorePassword, keyPassword)
                    .build()
        } else {
            // either verify server cert against trusted CA cert in the truststore
            logger.debug { "Using truststore ${AppConfig.truststore} for verification of server cert" }

            // load trust store
            val truststore = KeyStore.getInstance(KeyStore.getDefaultType())
            val fis = FileInputStream(AppConfig.truststore)
            val truststorePassword = AppConfig.truststorePassword.toCharArray()
            truststore.load(fis, truststorePassword)

            SSLContexts
                    .custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    // present client cert signed by the server CA cert's private key for server to verify
                    /*
                    Eris "CA" cert's private key -sign-> "local_node" cert (for server-side Eris node port 1337)
                    Eris "CA" cert's private key -sign-> "eris-api-client-cert-key-store" cert (for this client-side)
                     */
                    .loadKeyMaterial(keystore, keystorePassword, keyPassword)

                    .build()
        }

        customizeClient {
            setMaxConnTotal(1000) // Maximum number of socket connections.
            setMaxConnPerRoute(100) // Maximum number of requests for a specific endpoint route.

            // https
            setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
            setSSLContext(mySslContext)
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
                setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            }
            acceptContentTypes = listOf(ContentType.Application.Json, ContentType.Text.Plain)
        }
    }

    fun invokeErisCall(contractAddress: String, contractFunction: Function): List<Type<*>> {
        return runBlocking {
            val erisCallResponse = apiClient.post<ErisApi.ErisCallResponseDto> {
                url(URL("${AppConfig.erisApiUrl}/calls"))
                contentType(ContentType.Application.Json)
                body = ErisApi.ErisCallRequestDto(
                        address = contractAddress,
                        data = FunctionEncoder.encode(contractFunction).removePrefix("0x"))
            }
            FunctionReturnDecoder.decode(erisCallResponse.`return`, contractFunction.outputParameters)
        }
    }

    fun invokeErisTransact(privateKey: String, contractAddress: String, contractFunction: Function): String {
        return runBlocking {
            val erisTransactResponse = apiClient.post<ErisApi.ErisTransactResponseDto> {
                url(URL("${AppConfig.erisApiUrl}/unsafe/txpool?hold=false"))
                contentType(ContentType.Application.Json)
                body = ErisApi.ErisTransactRequestDto(
                        privKey = privateKey,
                        address = contractAddress,
                        data = FunctionEncoder.encode(contractFunction).removePrefix("0x"),
                        fee = 0L,
                        gasLimit = 999999999L)
            }
            erisTransactResponse.txHash
        }
    }

    fun invokeErisTransactAndHold(privateKey: String, contractAddress: String, contractFunction: Function): List<Type<*>> {
        return runBlocking {
            val erisTransactAndHoldResponse = apiClient.post<ErisApi.ErisTransactAndHoldResponseDto> {
                url(URL("${AppConfig.erisApiUrl}/unsafe/txpool?hold=true"))
                contentType(ContentType.Application.Json)
                body = ErisApi.ErisTransactRequestDto(
                        privKey = privateKey,
                        address = contractAddress,
                        data = FunctionEncoder.encode(contractFunction).removePrefix("0x"),
                        fee = 0L,
                        gasLimit = 999999999L)
            }
            FunctionReturnDecoder.decode(erisTransactAndHoldResponse.`return`, contractFunction.outputParameters)
        }
    }

}
