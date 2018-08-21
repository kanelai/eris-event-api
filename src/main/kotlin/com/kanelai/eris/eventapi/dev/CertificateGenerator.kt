package com.kanelai.eris.eventapi.dev

import io.ktor.util.generateCertificate
import java.io.File

object CertificateGenerator {
    @JvmStatic
    fun main(args: Array<String>) {
        val jksFile = File("config/eris-event-api.jks").apply {
            parentFile.mkdirs()
        }

        if (!jksFile.exists()) {
            generateCertificate(jksFile) // Generates the certificate
        }
    }
}
