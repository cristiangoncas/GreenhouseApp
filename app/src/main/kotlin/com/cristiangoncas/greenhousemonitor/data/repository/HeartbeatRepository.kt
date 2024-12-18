package com.cristiangoncas.greenhousemonitor.data.repository

import com.cristiangoncas.greenhousemonitor.data.local.model.CustomResult
import com.cristiangoncas.greenhousemonitor.data.local.model.HeartBeat
import com.cristiangoncas.greenhousemonitor.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface HeartbeatRepository {

    fun nextHeartBeat(): Flow<CustomResult<HeartBeat>>

    // TODO: All set actions need to return a Flow<Result> to catch errors and send them to the UI
    suspend fun setMaxTemp(maxTemp: Int): CustomResult<Unit>

    suspend fun setMinTemp(minTemp: Int): CustomResult<Unit>

    suspend fun setMorningTime(morningTime: Int): CustomResult<Unit>

    suspend fun setNightTime(nightTime: Int): CustomResult<Unit>

    suspend fun setNightTempDifference(tempDifference: Int): CustomResult<Unit>

    suspend fun setHealthCheck(): CustomResult<Unit>

    suspend fun resetDefaults(): CustomResult<Unit>

    suspend fun setHeartbeatPeriod(heartbeatPeriod: Int): CustomResult<Unit>
}

class HeartbeatRepositoryImpl(private val remoteDataSource: RemoteDataSource) :
    HeartbeatRepository {

    override fun nextHeartBeat(): Flow<CustomResult<HeartBeat>> {
        return flow { emit(remoteDataSource.nextHeartBeat()) }
    }

    override suspend fun setMaxTemp(maxTemp: Int): CustomResult<Unit> {
        return remoteDataSource.setMaxTemp(maxTemp)
    }

    override suspend fun setMinTemp(minTemp: Int): CustomResult<Unit> {
        return remoteDataSource.setMinTemp(minTemp)
    }

    override suspend fun setMorningTime(morningTime: Int): CustomResult<Unit> {
        return remoteDataSource.setMorningTime(morningTime)
    }

    override suspend fun setNightTime(nightTime: Int): CustomResult<Unit> {
        return remoteDataSource.setNightTime(nightTime)
    }

    override suspend fun setNightTempDifference(tempDifference: Int): CustomResult<Unit> {
        return remoteDataSource.setNightTempDifference(tempDifference)
    }

    override suspend fun setHealthCheck(): CustomResult<Unit> {
        return remoteDataSource.setHealthCheck()
    }

    override suspend fun resetDefaults(): CustomResult<Unit> {
        return remoteDataSource.resetDefaults()
    }

    override suspend fun setHeartbeatPeriod(heartbeatPeriod: Int): CustomResult<Unit> {
        return remoteDataSource.setHeartbeatPeriod(heartbeatPeriod)
    }
}
