package com.kanelai.eris.eventapi.client

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.kanelai.eris.eventapi.gson.GsonMillisInstantAdapter
import java.time.Instant

interface SmartContractApiResponse {
    val returnCode: String
}

data class QueueData(@JsonAdapter(GsonMillisInstantAdapter::class) @SerializedName("timestampMillis") val timestamp: Instant, val payload: String)

data class CountResponseData(val count: Int)
data class CountResponse(override val returnCode: String, val countResponse: CountResponseData? = null) : SmartContractApiResponse

data class QueueMessage(val offset: Int, val sender: String, @JsonAdapter(QueueDataDeserializer::class) val data: QueueData, val blockNumber: Int)
data class PeekResponseData(val item: QueueMessage)
data class PeekResponse(override val returnCode: String, val peekResponse: PeekResponseData? = null) : SmartContractApiResponse

data class RemoveFirstItemsResponse(override val returnCode: String) : SmartContractApiResponse

data class EnqueueResponse(override val returnCode: String) : SmartContractApiResponse

data class BlockNumberResponseData(val blockNumber: Int)
data class BlockNumberResponse(override val returnCode: String, val blockNumberResponse: BlockNumberResponseData? = null) : SmartContractApiResponse
