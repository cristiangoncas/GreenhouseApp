package com.cristiangoncas.greenhousemonitor.business.client

import com.cristiangoncas.greenhousemonitor.business.entity.HeartBeat
import com.cristiangoncas.greenhousemonitor.business.entity.LogEntry
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class ApiClient(
    private val client: HttpClient = HttpClient(),
    private val apiUrl: String
) : Api {

    override suspend fun getLogs24h(): List<LogEntry> {
        val response = client.get {
            url("$apiUrl/fetchLogs")
        }
        client.close()
        if (response.status.value in 200..299) {
            return Json.decodeFromString(response.body())
        } else {
            throw Exception(response.status.description)
        }
    }

    override suspend fun getAllLogs(): List<LogEntry> {
        val response = client.get {
            url("$apiUrl/fetchAllLogs")
        }
        client.close()
        if (response.status.value in 200..299) {
            return Json.decodeFromString(response.body())
        } else {
            throw Exception(response.status.description)
        }
    }

    override suspend fun nextHeartBeat(): HeartBeat {
        val response = client.get {
            url("$apiUrl/nextHeartBeat")
        }
        client.close()
        if (response.status.value in 200..299) {
            // TODO: Backend needs to return a common data structure shared across all APIs
            val body = response.body<String>()
            val jsonElement = Json.parseToJsonElement(body)
            val heartBeatValues = mutableMapOf<String, String>()
            jsonElement.jsonObject.entries.map {
                heartBeatValues.put(it.key, it.value.jsonPrimitive.content)
            }

            return HeartBeat(heartBeatValues)
        } else {
            throw Exception(response.status.description)
        }
    }

    override suspend fun setMaxTemp(maxTemp: Int) {
        val response = client.post {
            url("$apiUrl/setMaxTemp")
        }
        client.close()
        if (response.status.value in 200..299) {
            return
        } else {
            throw Exception(response.status.description)
        }
    }

    override suspend fun setMinTemp(minTemp: Int) {
        val response = client.post {
            url("$apiUrl/setMinTemp")
        }
        client.close()
        if (response.status.value in 200..299) {
            return
        } else {
            throw Exception(response.status.description)
        }
    }

    override suspend fun setMorningTime(morningTime: Int) {
        val response = client.post {
            url("$apiUrl/setMorningTime")
        }
        client.close()
        if (response.status.value in 200..299) {
            return
        } else {
            throw Exception(response.status.description)
        }
    }

    override suspend fun setNightTime(nightTime: Int) {
        val response = client.post {
            url("$apiUrl/setNightTime")
        }
        client.close()
        if (response.status.value in 200..299) {
            return
        } else {
            throw Exception(response.status.description)
        }
    }

    override suspend fun setNightTempDifference(tempDifference: Int) {
        val response = client.post {
            url("$apiUrl/setNightTempDifference")
        }
        client.close()
        if (response.status.value in 200..299) {
            return
        } else {
            throw Exception(response.status.description)
        }
    }

    override suspend fun setHealthCheck() {
        val response = client.post {
            url("$apiUrl/setHealthCheck")
        }
        client.close()
        if (response.status.value in 200..299) {
            return
        } else {
            throw Exception(response.status.description)
        }
    }

    override suspend fun resetDefaults() {
        val response = client.post {
            url("$apiUrl/resetDefaults")
        }
        client.close()
        if (response.status.value in 200..299) {
            return
        } else {
            throw Exception(response.status.description)
        }
    }

    override suspend fun setHeartbeatPeriod(heartbeatPeriod: Int) {
        val response = client.post {
            url("$apiUrl/setHeartbeatPeriod")
        }
        client.close()
        if (response.status.value in 200..299) {
            return
        } else {
            throw Exception(response.status.description)
        }
    }
}