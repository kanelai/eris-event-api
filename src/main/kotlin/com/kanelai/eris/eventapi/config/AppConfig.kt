package com.kanelai.eris.eventapi.config

import com.typesafe.config.ConfigFactory
import java.util.concurrent.TimeUnit

object AppConfig {
    private val rootConfig = ConfigFactory.load().getConfig("erisEventApi")

    private val erisApiConfig = rootConfig.getConfig("erisApi")
    val erisApiUrl = erisApiConfig.getString("url")!!
    val erisApiRequestTimeout = erisApiConfig.getDuration("requestTimeout", TimeUnit.MILLISECONDS)
    val erisApiAccountPrivateKey = erisApiConfig.getString("accountPrivateKey")!!
    val erisApiContractAddress = erisApiConfig.getString("contractAddress")!!
    val trustAllServerCert = erisApiConfig.getBoolean("trustAllServerCert")
    val truststore = erisApiConfig.getString("truststore")!!
    val truststorePassword = erisApiConfig.getString("truststorePassword")!!
    val keystore = erisApiConfig.getString("keystore")!!
    val keystorePassword = erisApiConfig.getString("keystorePassword")!!
    val keyPassword = erisApiConfig.getString("keyPassword")!!

    private val subscribeConfig = rootConfig.getConfig("subscribe")
    val subscribeEnabled = subscribeConfig.getBoolean("enabled")
    val subscribeTopic = subscribeConfig.getString("topic")!!
    val subscribeCheckBlockNumberInterval = subscribeConfig.getDuration("checkBlockNumberInterval", TimeUnit.MILLISECONDS)

    private val dbConfig = rootConfig.getConfig("db")
    val dbDriver = dbConfig.getString("driver")!!
    val dbJdbcUrl = dbConfig.getString("jdbcUrl")!!
    val dbUsername = dbConfig.getString("username")!!
    val dbPassword = dbConfig.getString("password")!!
}
