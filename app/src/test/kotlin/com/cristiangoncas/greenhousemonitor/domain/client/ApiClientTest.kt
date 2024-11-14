package com.cristiangoncas.greenhousemonitor.domain.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ApiClientTest {

    @Test
    fun testGetLogs24h() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = """[
                      {
                        "data": "75.00",
                        "event": "humidRead",
                        "id": 25964,
                        "time": "2024-11-12 12:54:07"
                      },
                      {
                        "data": "20.00",
                        "event": "tempRead",
                        "id": 25963,
                        "time": "2024-11-12 12:54:07"
                      }
                ]""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val apiClient = ApiClient(client = HttpClient(mockEngine), apiUrl = "http://localhost:8080")

        val logs = apiClient.getLogs24h()
        assert(logs.size == 2)
        val log = logs[0]
        assert(log.id == 25964)
        val log1 = logs[1]
        assert(log1.id == 25963)
    }

    @Test
    fun testGetAllLogs() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = """[
                        {
                            "data": "65.00",
                            "event": "humidRead",
                            "id": 21321,
                            "time": "2024-10-30 17:07:38"
                        },
                        {
                            "data": "20.00",
                            "event": "tempRead",
                            "id": 21320,
                            "time": "2024-10-30 17:07:38"
                        }
                ]""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val apiClient = ApiClient(client = HttpClient(mockEngine), apiUrl = "http://localhost:8080")

        val logs = apiClient.getAllLogs()
        assert(logs.size == 2)
        val log = logs[0]
        assert(log.id == 21321)
        val log1 = logs[1]
        assert(log1.id == 21320)
    }

    @Test
    fun testNextHeartBeat() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = """{"maxTemp": "22", "minTemp": "17"}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val apiClient = ApiClient(client = HttpClient(mockEngine), apiUrl = "http://localhost:8080")

        val heartBeat = apiClient.nextHeartBeat()
        assertNotNull(heartBeat.maxTemp)
        assertNotNull(heartBeat.minTemp)
        assertNull(heartBeat.morningTime)
        assertNull(heartBeat.nightTime)
        assert(heartBeat.maxTemp == "22")
        assert(heartBeat.minTemp == "17")
    }
}