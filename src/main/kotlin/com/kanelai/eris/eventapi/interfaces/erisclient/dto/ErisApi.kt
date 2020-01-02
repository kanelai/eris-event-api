package com.kanelai.eris.eventapi.interfaces.erisclient.dto

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.kanelai.eris.eventapi.application.util.json.GsonMillisInstantAdapter
import java.time.Instant

object ErisApi {

    data class ErisCallRequestDto(val address: String, val data: String)
    data class ErisCallResponseDto(val `return`: String, val gasUsed: Long)

    data class ErisTransactRequestDto(val privKey: String, val address: String, val data: String, val fee: Long, val gasLimit: Long)
    data class ErisTransactResponseDto(val txHash: String, val createsContract: Int, val contractAddr: String)
    data class ErisTransactAndHoldResponseCallData(val caller: String, val callee: String, val data: String, val value: Long, val gas: Long)
    data class ErisTransactAndHoldResponseDto(val callData: ErisTransactAndHoldResponseCallData, val origin: String, val txId: String, val `return`: String, val exception: String)

    data class PeekResponseDto(val sender: String, val data: String, val blockNumber: Int)

    data class QueueMessageDataDto(@JsonAdapter(GsonMillisInstantAdapter::class) @SerializedName("timestampMillis") val timestamp: Instant, val payload: String)

}
