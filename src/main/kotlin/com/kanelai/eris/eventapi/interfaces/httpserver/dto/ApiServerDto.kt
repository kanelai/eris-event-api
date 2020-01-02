package com.kanelai.eris.eventapi.interfaces.httpserver.dto

import com.google.gson.annotations.JsonAdapter
import com.kanelai.eris.eventapi.application.util.json.GsonIso8601InstantAdapter
import java.time.Instant

object HttpApi {

    interface CommonResponseDto {
        val returnCode: String
    }

    data class EnqueueResponseDataDto(@JsonAdapter(GsonIso8601InstantAdapter::class) val timestamp: Instant)
    data class EnqueueResponseDto(override val returnCode: String, val enqueueResponse: EnqueueResponseDataDto? = null) : CommonResponseDto

    data class DequeueResponseDataDto(val sender: String, @JsonAdapter(GsonIso8601InstantAdapter::class) val timestamp: Instant, val blockNumber: Int, val payload: String)
    data class DequeueResponseDto(override val returnCode: String, val dequeueResponse: DequeueResponseDataDto? = null) : CommonResponseDto

    data class CountResponseDataDto(val count: Int)
    data class CountResponseDto(override val returnCode: String, val countResponse: CountResponseDataDto? = null) : CommonResponseDto

    data class ExceptionResponseDto(override val returnCode: String) : CommonResponseDto

}