package com.cristiangoncas.greenhousemonitor.data.remote.client

import com.cristiangoncas.greenhousemonitor.data.local.model.CustomResult
import com.cristiangoncas.greenhousemonitor.data.local.model.HeartBeat
import com.cristiangoncas.greenhousemonitor.data.remote.model.RemoteLogEntry
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

interface Api {

    suspend fun getLastLog(): CustomResult<RemoteLogEntry>

    suspend fun getLogs24h(): CustomResult<List<RemoteLogEntry>>

    suspend fun getAllLogs(): CustomResult<List<RemoteLogEntry>>

    suspend fun nextHeartBeat(): CustomResult<HeartBeat>

    suspend fun setMaxTemp(maxTemp: Int): CustomResult<Unit>

    suspend fun setMinTemp(minTemp: Int): CustomResult<Unit>

    suspend fun setMorningTime(morningTime: Int): CustomResult<Unit>

    suspend fun setNightTime(nightTime: Int): CustomResult<Unit>

    suspend fun setNightTempDifference(tempDifference: Int): CustomResult<Unit>

    suspend fun setHealthCheck(): CustomResult<Unit>

    suspend fun resetDefaults(): CustomResult<Unit>

    suspend fun setHeartbeatPeriod(heartbeatPeriod: Int): CustomResult<Unit>
}

class ApiImpl(
    private val client: HttpClient = HttpClient(),
    private val apiUrl: String
) : Api {

    override suspend fun getLastLog(): CustomResult<RemoteLogEntry> {
        val response = client.get {
            url("$apiUrl/fetchLastLog")
        }
        if (response.status.value in 200..299) {
            return CustomResult.Success(Json.decodeFromString(response.body()))
        } else {
            return CustomResult.Error(response.status.description)
        }
    }

    override suspend fun getLogs24h(): CustomResult<List<RemoteLogEntry>> {
        val response = client.get {
            url("$apiUrl/fetchLogs")
        }
        if (response.status.value in 200..299) {
            return CustomResult.Success(
                Json.decodeFromString(response.body())
            )
        } else {
            return CustomResult.Error(response.status.description)
        }
    }

    override suspend fun getAllLogs(): CustomResult<List<RemoteLogEntry>> {
        val response = client.get {
            url("$apiUrl/fetchAllLogs")
        }
        if (response.status.value in 200..299) {
            return CustomResult.Success(
                Json.decodeFromString(response.body())
            )
        } else {
            return CustomResult.Error(response.status.description)
        }
    }

    override suspend fun nextHeartBeat(): CustomResult<HeartBeat> {
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
            return CustomResult.Success(heartBeat)
        } else {
            return CustomResult.Error(response.status.description)
        }
    }

    override suspend fun setMaxTemp(maxTemp: Int): CustomResult<Unit> {
        val response = client.post {
            url("$apiUrl/setMaxTemp")
            contentType(ContentType.Application.Json)
            setBody("""{"maxTemp":$maxTemp}""")
        }
        if (response.status.value !in 200..299) {
            return CustomResult.Error(response.status.description)
        } else {
            return CustomResult.Success(Unit)
        }
    }

    override suspend fun setMinTemp(minTemp: Int): CustomResult<Unit> {
        val response = client.post {
            url("$apiUrl/setMinTemp")
            contentType(ContentType.Application.Json)
            setBody("""{"minTemp":$minTemp}""")
        }
        if (response.status.value !in 200..299) {
            return CustomResult.Error(response.status.description)
        } else {
            return CustomResult.Success(Unit)
        }
    }

    override suspend fun setMorningTime(morningTime: Int): CustomResult<Unit> {
        val response = client.post {
            url("$apiUrl/setMorningTime")
            contentType(ContentType.Application.Json)
            setBody("""{"morningTime":$morningTime}""")
        }
        if (response.status.value !in 200..299) {
            return CustomResult.Error(response.status.description)
        } else {
            return CustomResult.Success(Unit)
        }
    }

    override suspend fun setNightTime(nightTime: Int): CustomResult<Unit> {
        val response = client.post {
            url("$apiUrl/setNightTime")
            contentType(ContentType.Application.Json)
            setBody("""{"nightTime":$nightTime}""")
        }
        if (response.status.value !in 200..299) {
            return CustomResult.Error(response.status.description)
        } else {
            return CustomResult.Success(Unit)
        }
    }

    override suspend fun setNightTempDifference(tempDifference: Int): CustomResult<Unit> {
        val response = client.post {
            url("$apiUrl/setNightTempDifference")
            contentType(ContentType.Application.Json)
            setBody("""{"nightTempDifference":$tempDifference}""".trimMargin())
        }
        if (response.status.value !in 200..299) {
            return CustomResult.Error(response.status.description)
        } else {
            return CustomResult.Success(Unit)
        }
    }

    override suspend fun setHealthCheck(): CustomResult<Unit> {
        val response = client.post {
            url("$apiUrl/setHealthCheck")
            contentType(ContentType.Application.Json)
            setBody("""{"healthCheck":1}""")
        }
        if (response.status.value !in 200..299) {
            return CustomResult.Error(response.status.description)
        } else {
            return CustomResult.Success(Unit)
        }
    }

    override suspend fun resetDefaults(): CustomResult<Unit> {
        val response = client.post {
            url("$apiUrl/resetDefaults")
            contentType(ContentType.Application.Json)
            setBody("""{"resetDefaults":1}""")
        }
        if (response.status.value !in 200..299) {
            return CustomResult.Error(response.status.description)
        } else {
            return CustomResult.Success(Unit)
        }
    }

    override suspend fun setHeartbeatPeriod(heartbeatPeriod: Int): CustomResult<Unit> {
        val response = client.post {
            url("$apiUrl/setHeartbeatPeriod")
            contentType(ContentType.Application.Json)
            setBody("""{"heartbeatPeriod":$heartbeatPeriod}""")
        }
        if (response.status.value !in 200..299) {
            return CustomResult.Error(response.status.description)
        } else {
            return CustomResult.Success(Unit)
        }
    }
}
