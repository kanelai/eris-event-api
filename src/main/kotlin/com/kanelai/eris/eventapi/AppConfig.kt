package com.kanelai.eris.eventapi

import com.typesafe.config.ConfigFactory
import java.util.concurrent.TimeUnit

object AppConfig {
    private val rootConfig = ConfigFactory.load().getConfig("erisEventApi")

    private val smartContractApiConfig = rootConfig.getConfig("smartContractApi")
    val smartContractApiUrl = smartContractApiConfig.getString("url")!!
    val smartContractApiRequestTimeout = smartContractApiConfig.getDuration("requestTimeout", TimeUnit.MILLISECONDS)

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
