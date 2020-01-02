package com.kanelai.eris.eventapi.application.util.json

import com.google.gson.*
import java.lang.reflect.Type
import java.text.ParseException
import java.time.Instant
import java.time.format.DateTimeFormatter

class GsonIso8601InstantAdapter : JsonSerializer<Instant>, JsonDeserializer<Instant> {

    override fun serialize(src: Instant?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(DateTimeFormatter.ISO_INSTANT.format(src))
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Instant {
        if (json !is JsonPrimitive) throw JsonParseException("The date should be a string value")
        try {
            return Instant.parse(json.asString)
        } catch (e: ParseException) {
            throw JsonSyntaxException(json.asString, e)
        }
    }

}
