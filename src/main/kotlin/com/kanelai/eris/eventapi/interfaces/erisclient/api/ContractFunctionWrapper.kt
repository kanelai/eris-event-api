package com.kanelai.eris.eventapi.interfaces.erisclient.api

import com.kanelai.eris.eventapi.config.AppConfig
import com.kanelai.eris.eventapi.interfaces.erisclient.dto.ErisApi
import mu.KotlinLogging
import org.web3j.abi.datatypes.Uint
import org.web3j.abi.datatypes.Utf8String
import java.math.BigInteger

object ContractFunctionWrapper {

    private val logger = KotlinLogging.logger {}

    private val privateKey = AppConfig.erisApiAccountPrivateKey
    private val contractAddress = AppConfig.erisApiContractAddress

    fun getCount(topic: String): Int {
        val getCountResponseParams = ErisApiClient.invokeErisCall(contractAddress, ContractFunctionDef().getCount(Utf8String(topic)))
        val count = (getCountResponseParams[0] as Uint).value.toInt()
        logger.trace { "Blockchain event queue count (topic: $topic): $count" }
        return count
    }

    fun enqueue(topic: String, data: String) {
        ErisApiClient.invokeErisTransact(privateKey, contractAddress, ContractFunctionDef().enqueue(Utf8String(topic), Utf8String(data)))
        logger.trace { "Enqueued to blockchain event queue (topic: $topic): $data" }
    }

    fun peek(topic: String, offset: Int): ErisApi.PeekResponseDto {
        val peekResponseParams = ErisApiClient.invokeErisCall(contractAddress, ContractFunctionDef().peek(Utf8String(topic), Uint(BigInteger.valueOf(offset.toLong()))))
        val peekResponse = ErisApi.PeekResponseDto(
                peekResponseParams[0].toString(),
                peekResponseParams[1].toString(),
                (peekResponseParams[2] as Uint).value.toInt())
        logger.trace { "Peek (topic: $topic, offset: $offset): $peekResponse" }
        return peekResponse
    }

    fun removeFirstItems(topic: String, count: Int) {
        ErisApiClient.invokeErisTransactAndHold(privateKey, contractAddress, ContractFunctionDef().removeFirstItems(Utf8String(topic), Uint(BigInteger.valueOf(count.toLong()))))
        logger.trace { "Removed first $count items (topic: $topic)" }
    }

    fun getBlockNumber(): Int {
        val getBlockNumberResponseParams = ErisApiClient.invokeErisCall(contractAddress, ContractFunctionDef().getBlockNumber())
        val blockNumber = (getBlockNumberResponseParams[0] as Uint).value.toInt()
        logger.trace { "Block number: $blockNumber" }
        return blockNumber
    }

}
