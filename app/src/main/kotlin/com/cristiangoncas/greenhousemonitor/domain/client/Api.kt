package com.cristiangoncas.greenhousemonitor.domain.client

import com.cristiangoncas.greenhousemonitor.domain.entity.HeartBeat
import com.cristiangoncas.greenhousemonitor.domain.entity.LogEntry
import com.cristiangoncas.greenhousemonitor.domain.entity.RemoteLogEntry

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