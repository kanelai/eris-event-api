package com.kanelai.eris.eventapi.infrastructure.persistence.exposed

import com.kanelai.eris.eventapi.config.AppConfig
import com.kanelai.eris.eventapi.domain.model.QueueTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.createMissingTablesAndColumns
import org.jetbrains.exposed.sql.SchemaUtils.withDataBaseLock
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseUtil {

    fun initDb() {
        Class.forName(AppConfig.dbDriver)
        val config = HikariConfig()
        config.jdbcUrl = AppConfig.dbJdbcUrl
        config.username = AppConfig.dbUsername
        config.password = AppConfig.dbPassword

        val ds = HikariDataSource(config)

        Database.connect(ds)

        transaction {
            addLogger(Slf4jSqlDebugLogger)

            withDataBaseLock {
                createMissingTablesAndColumns(QueueTable)
            }
        }
    }

}
