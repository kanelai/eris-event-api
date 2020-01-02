package com.kanelai.eris.eventapi

import com.kanelai.eris.eventapi.application.impl.BlockchainQueueCheckerServiceImpl
import com.kanelai.eris.eventapi.config.AppConfig
import com.kanelai.eris.eventapi.infrastructure.persistence.exposed.DatabaseUtil
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlin.concurrent.thread

object App {

    @JvmStatic
    fun main(args: Array<String>) {

        val env = commandLineEnvironment(args)

        if (AppConfig.subscribeEnabled) {
            println("Starting scheduler")

            // init DB
            DatabaseUtil.initDb()

            thread {
                // setup scheduler to poll Eris
                BlockchainQueueCheckerServiceImpl.startLoop()
            }
        }

        println("Starting API server")
        embeddedServer(Netty, env).start(wait = true)
    }

}
