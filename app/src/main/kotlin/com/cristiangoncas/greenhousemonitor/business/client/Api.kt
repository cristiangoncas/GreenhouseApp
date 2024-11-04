package com.cristiangoncas.greenhousemonitor.business.client

import com.cristiangoncas.greenhousemonitor.business.entity.HeartBeat
import com.cristiangoncas.greenhousemonitor.business.entity.LogEntry

interface Api {

    suspend fun getLogs24h(): List<LogEntry>

    suspend fun getAllLogs(): List<LogEntry>

    suspend fun nextHeartBeat(): HeartBeat

    suspend fun setMaxTemp(maxTemp: Int): Boolean

    suspend fun setMinTemp(minTemp: Int)

    suspend fun setMorningTime(morningTime: Int)

    suspend fun setNightTime(nightTime: Int)

    suspend fun setNightTempDifference(tempDifference: Int)

    suspend fun setHealthCheck()

    suspend fun resetDefaults()

    suspend fun setHeartbeatPeriod(heartbeatPeriod: Int)
}