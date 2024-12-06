package com.cristiangoncas.greenhousemonitor.data.repository

import com.cristiangoncas.greenhousemonitor.data.local.model.HeartBeat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface HeartbeatRepository {

    fun nextHeartBeat(): Flow<HeartBeat>

    suspend fun setMaxTemp(maxTemp: Int)

    suspend fun setMinTemp(minTemp: Int)

    suspend fun setMorningTime(morningTime: Int)

    suspend fun setNightTime(nightTime: Int)

    suspend fun setNightTempDifference(tempDifference: Int)

    suspend fun setHealthCheck()

    suspend fun resetDefaults()

    suspend fun setHeartbeatPeriod(heartbeatPeriod: Int)
}

class HeartbeatRepositoryImpl(private val remoteRepository: RemoteRepository) :
    HeartbeatRepository {

    override fun nextHeartBeat(): Flow<HeartBeat> {
        return flow { emit(remoteRepository.nextHeartBeat()) }
    }

    override suspend fun setMaxTemp(maxTemp: Int) {
        remoteRepository.setMaxTemp(maxTemp)
    }

    override suspend fun setMinTemp(minTemp: Int) {
        remoteRepository.setMinTemp(minTemp)
    }

    override suspend fun setMorningTime(morningTime: Int) {
        remoteRepository.setMorningTime(morningTime)
    }

    override suspend fun setNightTime(nightTime: Int) {
        remoteRepository.setNightTime(nightTime)
    }

    override suspend fun setNightTempDifference(tempDifference: Int) {
        remoteRepository.setNightTempDifference(tempDifference)
    }

    override suspend fun setHealthCheck() {
        remoteRepository.setHealthCheck()
    }

    override suspend fun resetDefaults() {
        remoteRepository.resetDefaults()
    }

    override suspend fun setHeartbeatPeriod(heartbeatPeriod: Int) {
        remoteRepository.setHeartbeatPeriod(heartbeatPeriod)
    }
}
