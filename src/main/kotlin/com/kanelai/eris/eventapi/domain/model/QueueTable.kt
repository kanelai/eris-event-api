package com.kanelai.eris.eventapi.domain.model

import org.jetbrains.exposed.sql.Table

object QueueTable : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val sender = varchar("sender", 50)
    val timestamp = datetime("timestamp").index()
    val blockNumber = integer("block_number")
    val payload = varchar("payload", 65535)
}
