package com.cristiangoncas.greenhousemonitor.data.remote

import com.cristiangoncas.greenhousemonitor.data.local.model.CustomResult
import com.cristiangoncas.greenhousemonitor.data.remote.client.ApiImpl
import com.cristiangoncas.greenhousemonitor.data.local.model.HeartBeat
import com.cristiangoncas.greenhousemonitor.data.remote.model.RemoteLogEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface RemoteDataSource {

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

class RemoteDataSourceImpl(private val api: ApiImpl) : RemoteDataSource {

    override suspend fun getLastLog(): CustomResult<RemoteLogEntry> {
        return api.getLastLog()
    }

    override suspend fun getLogs24h(): CustomResult<List<RemoteLogEntry>> {
        return api.getLogs24h()
    }

    override suspend fun getAllLogs(): CustomResult<List<RemoteLogEntry>> {
        return api.getAllLogs()
    }

    override suspend fun nextHeartBeat(): CustomResult<HeartBeat> {
        return api.nextHeartBeat()
    }

    override suspend fun setMaxTemp(maxTemp: Int): CustomResult<Unit> {
        return api.setMaxTemp(maxTemp)
    }

    override suspend fun setMinTemp(minTemp: Int): CustomResult<Unit> {
        return api.setMinTemp(minTemp)
    }

    override suspend fun setMorningTime(morningTime: Int): CustomResult<Unit> {
        return api.setMorningTime(morningTime)
    }

    override suspend fun setNightTime(nightTime: Int): CustomResult<Unit> {
        return api.setNightTime(nightTime)
    }

    override suspend fun setNightTempDifference(tempDifference: Int): CustomResult<Unit> {
        return api.setNightTempDifference(tempDifference)
    }

    override suspend fun setHealthCheck(): CustomResult<Unit> {
        return api.setHealthCheck()
    }

    override suspend fun resetDefaults(): CustomResult<Unit> {
        return api.resetDefaults()
    }

    override suspend fun setHeartbeatPeriod(heartbeatPeriod: Int): CustomResult<Unit> {
        return api.setHeartbeatPeriod(heartbeatPeriod)
    }
}
