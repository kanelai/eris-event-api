package com.kanelai.eris.eventapi.gson

import com.google.gson.*
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class GsonIso8601InstantAdapter : JsonSerializer<Instant>, JsonDeserializer<Instant> {

    private val iso8601Format: DateFormat

    init {
        this.iso8601Format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        this.iso8601Format.timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun serialize(src: Instant?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement = JsonPrimitive(iso8601Format.format(src))

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Instant {
        if (json !is JsonPrimitive) throw JsonParseException("The date should be a string value")
        try {
            return iso8601Format.parse(json.asString).toInstant()
        } catch (e: ParseException) {
            throw JsonSyntaxException(json.asString, e)
        }
    }

}
