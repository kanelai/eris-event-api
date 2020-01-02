package com.kanelai.eris.eventapi.interfaces.erisclient.api

import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.Function

class ContractFunctionDef {

    fun isFull(topic: Utf8String) = Function("isFull",
            listOf(topic),
            listOf(object : TypeReference<Bool>() {}))

    fun getCount(topic: Utf8String) = Function("getCount",
            listOf(topic),
            listOf(object : TypeReference<Uint>() {}))

    fun getCapacity(topic: Utf8String) = Function("getCapacity",
            listOf(topic),
            listOf(object : TypeReference<Uint>() {}))

    fun enqueue(topic: Utf8String, data: Utf8String) = Function("enqueue",
            listOf(topic, data),
            emptyList())

    fun peek(topic: Utf8String, offset: Uint) = Function("peek",
            listOf(topic, offset),
            listOf(object : TypeReference<Address>() {}, object : TypeReference<Utf8String>() {}, object : TypeReference<Uint>() {}))

    fun removeFirstItem(topic: Utf8String) = Function("removeFirstItem",
            listOf(topic),
            emptyList())

    fun removeFirstItems(topic: Utf8String, count: Uint) = Function("removeFirstItems",
            listOf(topic, count),
            emptyList())

    fun removeAllItems(topic: Utf8String) = Function("removeAllItems",
            listOf(topic),
            emptyList())

    fun getBlockNumber() = Function("getBlockNumber",
            emptyList(),
            listOf(object : TypeReference<Uint>() {}))

    // internal functions

    fun firstIndex_(topic: Utf8String) = Function("_firstIndex",
            listOf(topic),
            listOf(object : TypeReference<Uint>() {}))

    fun lastIndex_(topic: Utf8String) = Function("_lastIndex",
            listOf(topic),
            listOf(object : TypeReference<Uint>() {}))

}
