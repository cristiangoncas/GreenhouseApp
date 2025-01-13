package com.cristiangoncas.data.repository

import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.greenhousemonitor.domain.models.HeartBeat
import com.cristiangoncas.data.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface HeartbeatRepository {

    fun nextHeartBeat(): Flow<CustomResult<HeartBeat>>

    fun setMaxTemp(maxTemp: Int): Flow<CustomResult<Unit>>

    fun setMinTemp(minTemp: Int): Flow<CustomResult<Unit>>

    fun setMorningTime(morningTime: Int): Flow<CustomResult<Unit>>

    fun setNightTime(nightTime: Int): Flow<CustomResult<Unit>>

    fun setNightTempDifference(tempDifference: Int): Flow<CustomResult<Unit>>

    fun setHealthCheck(): Flow<CustomResult<Unit>>

    fun resetDefaults(): Flow<CustomResult<Unit>>

    fun setHeartbeatPeriod(heartbeatPeriod: Int): Flow<CustomResult<Unit>>
}

class HeartbeatRepositoryImpl(private val remoteDataSource: RemoteDataSource) :
    HeartbeatRepository {

    override fun nextHeartBeat(): Flow<CustomResult<HeartBeat>> {
        return flow { emit(remoteDataSource.nextHeartBeat()) }.flowOn(Dispatchers.IO)
    }

    override fun setMaxTemp(maxTemp: Int): Flow<CustomResult<Unit>> {
        return flow { emit(remoteDataSource.setMaxTemp(maxTemp)) }.flowOn(Dispatchers.IO)
    }

    override fun setMinTemp(minTemp: Int): Flow<CustomResult<Unit>> {
        return flow { emit(remoteDataSource.setMinTemp(minTemp)) }.flowOn(Dispatchers.IO)
    }

    override fun setMorningTime(morningTime: Int): Flow<CustomResult<Unit>> {
        return flow { emit(remoteDataSource.setMorningTime(morningTime)) }.flowOn(Dispatchers.IO)
    }

    override fun setNightTime(nightTime: Int): Flow<CustomResult<Unit>> {
        return flow { emit(remoteDataSource.setNightTime(nightTime)) }.flowOn(Dispatchers.IO)
    }

    override fun setNightTempDifference(tempDifference: Int): Flow<CustomResult<Unit>> {
        return flow { emit(remoteDataSource.setNightTempDifference(tempDifference)) }.flowOn(Dispatchers.IO)
    }

    override fun setHealthCheck(): Flow<CustomResult<Unit>> {
        return flow { emit(remoteDataSource.setHealthCheck()) }.flowOn(Dispatchers.IO)
    }

    override fun resetDefaults(): Flow<CustomResult<Unit>> {
        return flow { emit(remoteDataSource.resetDefaults()) }.flowOn(Dispatchers.IO)
    }

    override fun setHeartbeatPeriod(heartbeatPeriod: Int): Flow<CustomResult<Unit>> {
        return flow { emit(remoteDataSource.setHeartbeatPeriod(heartbeatPeriod)) }.flowOn(Dispatchers.IO)
    }
}
