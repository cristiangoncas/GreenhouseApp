package com.cristiangoncas.greenhousemonitor.data.repository

import com.cristiangoncas.greenhousemonitor.data.remote.client.ApiClient
import com.cristiangoncas.greenhousemonitor.data.local.model.HeartBeat
import com.cristiangoncas.greenhousemonitor.data.remote.model.RemoteLogEntry

interface RemoteRepository {

    suspend fun getLogs24h(): List<RemoteLogEntry>

    suspend fun getAllLogs(): List<RemoteLogEntry>

    suspend fun nextHeartBeat(): HeartBeat

    suspend fun setMaxTemp(maxTemp: Int)

    suspend fun setMinTemp(minTemp: Int)

    suspend fun setMorningTime(morningTime: Int)

    suspend fun setNightTime(nightTime: Int)

    suspend fun setNightTempDifference(tempDifference: Int)

    suspend fun setHealthCheck()

    suspend fun resetDefaults()

    suspend fun setHeartbeatPeriod(heartbeatPeriod: Int)

}

class RemoteRepositoryImpl(private val api: ApiClient) : RemoteRepository {
    override suspend fun getLogs24h(): List<RemoteLogEntry> {
        return api.getLogs24h()
    }

    override suspend fun getAllLogs(): List<RemoteLogEntry> {
        return api.getAllLogs()
    }

    override suspend fun nextHeartBeat(): HeartBeat {
        return api.nextHeartBeat()
    }

    override suspend fun setMaxTemp(maxTemp: Int) {
        return api.setMaxTemp(maxTemp)
    }

    override suspend fun setMinTemp(minTemp: Int) {
        return api.setMinTemp(minTemp)
    }

    override suspend fun setMorningTime(morningTime: Int) {
        return api.setMorningTime(morningTime)
    }

    override suspend fun setNightTime(nightTime: Int) {
        return api.setNightTime(nightTime)
    }

    override suspend fun setNightTempDifference(tempDifference: Int) {
        return api.setNightTempDifference(tempDifference)
    }

    override suspend fun setHealthCheck() {
        return api.setHealthCheck()
    }

    override suspend fun resetDefaults() {
        return api.resetDefaults()
    }

    override suspend fun setHeartbeatPeriod(heartbeatPeriod: Int) {
        return api.setHeartbeatPeriod(heartbeatPeriod)
    }
}
