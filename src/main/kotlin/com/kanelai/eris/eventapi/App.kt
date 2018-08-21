package com.kanelai.eris.eventapi

import com.kanelai.eris.eventapi.client.BlockchainQueueChecker
import com.kanelai.eris.eventapi.db.QueueTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.createMissingTablesAndColumns
import org.jetbrains.exposed.sql.SchemaUtils.withDataBaseLock
import org.jetbrains.exposed.sql.Slf4jSqlLogger
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.concurrent.thread

object App {

    @JvmStatic
    fun main(args: Array<String>) {

        val env = commandLineEnvironment(args)

        if (AppConfig.subscribeEnabled) {
            // init DB
            initDb()

            thread {
                // setup scheduler to poll Eris
                BlockchainQueueChecker.startLoop()
            }
        }

        embeddedServer(Netty, env).start()
    }

    private fun initDb() {
        Class.forName(AppConfig.dbDriver);
        val config = HikariConfig()
        config.jdbcUrl = AppConfig.dbJdbcUrl
        config.username = AppConfig.dbUsername
        config.password = AppConfig.dbPassword

        val ds = HikariDataSource(config)

        Database.connect(ds)

        transaction {
            logger.addLogger(Slf4jSqlLogger)

            withDataBaseLock {
                createMissingTablesAndColumns(QueueTable)
            }
        }
    }

}
