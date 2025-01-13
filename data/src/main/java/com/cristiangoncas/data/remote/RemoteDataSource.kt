package com.cristiangoncas.data.remote

import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.greenhousemonitor.domain.models.HeartBeat
import com.cristiangoncas.greenhousemonitor.domain.models.LogEntry

interface RemoteDataSource {

    suspend fun getLastLog(): CustomResult<LogEntry>

    suspend fun getLogs24h(): CustomResult<List<LogEntry>>

    suspend fun getAllLogs(): CustomResult<List<LogEntry>>

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
