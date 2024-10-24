package com.cristiangoncas.greenhousemonitor.business.client

import com.cristiangoncas.greenhousemonitor.BuildConfig
import com.cristiangoncas.greenhousemonitor.business.entity.LogEntry
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.path
import kotlinx.serialization.json.Json

class ApiClient {

    private val apiUrl = BuildConfig.API_IP
    private val client = HttpClient()

    suspend fun getLogs24h(): List<LogEntry> {
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
}