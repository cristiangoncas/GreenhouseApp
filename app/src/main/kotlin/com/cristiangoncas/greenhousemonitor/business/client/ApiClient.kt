package com.cristiangoncas.greenhousemonitor.business.client

import com.cristiangoncas.greenhousemonitor.business.entity.HeartBeat
import com.cristiangoncas.greenhousemonitor.business.entity.LogEntry
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.decodeFromString
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
        if (response.status.value in 200..299) {
            // TODO: Backend needs to return a common data structure shared across all APIs
            val body = response.body<String>()
            val json = Json {
                ignoreUnknownKeys = true
            }
            val heartBeat = json.decodeFromString<HeartBeat>(body)
            return heartBeat
        } else {
            throw Exception(response.status.description)
        }
    }

    override suspend fun setMaxTemp(maxTemp: Int): Boolean {
        try {
            val response = client.post {
                url("$apiUrl/setMaxTemp")
                contentType(ContentType.Application.Json)
                setBody("""{"maxTemp":$maxTemp}""")
            }
            if (response.status.value in 200..299) {
                return true
            } else {
                throw Exception(response.status.description)
            }
        } catch (e: Exception) {
            println(e.message)
            throw e

        }
    }

    override suspend fun setMinTemp(minTemp: Int) {
        val response = client.post {
            url("$apiUrl/setMinTemp")
        }
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
        if (response.status.value in 200..299) {
            return
        } else {
            throw Exception(response.status.description)
        }
    }
}