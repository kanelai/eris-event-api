package com.kanelai.eris.eventapi.db

import org.jetbrains.exposed.sql.Table

object QueueTable : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val sender = varchar("sender", 50)
    val timestamp = datetime("timestamp").index()
    val payload = varchar("payload", 65535)
}
