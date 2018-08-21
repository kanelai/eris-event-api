package com.kanelai.eris.eventapi.server

import com.google.gson.annotations.JsonAdapter
import com.kanelai.eris.eventapi.gson.GsonIso8601InstantAdapter
import java.time.Instant

interface ServerApiResponse {
    val returnCode: String
}

data class EnqueueResponseData(@JsonAdapter(GsonIso8601InstantAdapter::class) val timestamp: Instant)
data class EnqueueResponse(override val returnCode: String, val enqueueResponse: EnqueueResponseData? = null) : ServerApiResponse

data class DequeueResponseData(val sender: String, @JsonAdapter(GsonIso8601InstantAdapter::class) val timestamp: Instant, val payload: String)
data class DequeueResponse(override val returnCode: String, val dequeueResponse: DequeueResponseData? = null) : ServerApiResponse

data class CountResponseData(val count: Int)
data class CountResponse(override val returnCode: String, val countResponse: CountResponseData? = null) : ServerApiResponse
