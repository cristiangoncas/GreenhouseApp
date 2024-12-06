package com.cristiangoncas.greenhousemonitor.data.remote.client

import com.cristiangoncas.greenhousemonitor.data.local.model.HeartBeat
import com.cristiangoncas.greenhousemonitor.data.remote.model.RemoteLogEntry

interface Api {

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
