package com.kanelai.eris.eventapi.client

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.Instant

class QueueDataDeserializer : JsonDeserializer<QueueData> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): QueueData {
        return try {
            Gson().fromJson<QueueData>(json.asString, QueueData::class.java)
        } catch (ex: Exception) {
            QueueData(Instant.now(), json.asString)
        }
    }

}
