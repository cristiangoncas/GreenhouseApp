package com.cristiangoncas.greenhousemonitor.ui.screen.logs

import com.cristiangoncas.data.remote.ConnectivityDataSource
import com.cristiangoncas.data.repository.LogsRepository
import com.cristiangoncas.greenhousemonitor.domain.models.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.greenhousemonitor.domain.models.HeaterOnOffCounts
import com.cristiangoncas.greenhousemonitor.domain.models.LogEntry
import com.cristiangoncas.greenhousemonitor.framework.remote.client.Api
import com.cristiangoncas.greenhousemonitor.framework.remote.model.RemoteHeartBeat
import com.cristiangoncas.greenhousemonitor.framework.remote.model.RemoteLogEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class MockConnectivityDataSource(
    override val isConnected: StateFlow<Boolean> = MutableStateFlow(true)
) : ConnectivityDataSource

class MockLogsRepository() : LogsRepository {
    override fun fetchLastLogEntries(): Flow<CustomResult<List<LogEntry>>> {
        return flow {
            emit(
                CustomResult.Success(
                    listOf(
                        LogEntry(
                            id = 1,
                            timestamp = 0,
                            date = "12-12-2022",
                            time = "12:12:12",
                            data = "This is a test!",
                            event = "Heater on"
                        ),
                        LogEntry(
                            id = 2,
                            timestamp = 0,
                            date = "12-12-2022",
                            time = "12:12:12",
                            data = "This is a test2!",
                            event = "Humidity read"
                        )
                    )
                )
            )
        }
    }

    override fun fetchAveragesByPeriodOfTime(period: Long): Flow<CustomResult<AverageTempHumid>> {
        return flow {
            emit(
                CustomResult.Success(
                    AverageTempHumid(
                        avgTempRead = 18.00f,
                        avgHumidRead = 56.00f
                    )
                )
            )
        }
    }

    override fun fetchHeaterEventsByPeriodOfTime(period: Long): Flow<CustomResult<HeaterOnOffCounts>> {
        return flow {
            emit(
                CustomResult.Success(
                    HeaterOnOffCounts(
                        heaterOnCount = 1,
                        heaterOffCount = 1
                    )
                )
            )
        }
    }
}

class MockApi : Api {
    override suspend fun getLastLog(): CustomResult<RemoteLogEntry> {
        TODO("Not yet implemented")
    }

    override suspend fun getLogs24h(): CustomResult<List<RemoteLogEntry>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLogs(): CustomResult<List<RemoteLogEntry>> {
        return CustomResult.Success(
            listOf(
                RemoteLogEntry(
                    id = 1,
                    time = "2025-02-10 13:11:04",
                    data = "This is a test!",
                    event = "Heater on"
                ),
                RemoteLogEntry(
                    id = 2,
                    time = "2025-02-10 13:11:04",
                    data = "This is a test2!",
                    event = "Humidity read"
                )
            )
        )
    }

    override suspend fun nextHeartBeat(): CustomResult<RemoteHeartBeat> {
        TODO("Not yet implemented")
    }

    override suspend fun setMaxTemp(maxTemp: Int): CustomResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun setMinTemp(minTemp: Int): CustomResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun setMorningTime(morningTime: Int): CustomResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun setNightTime(nightTime: Int): CustomResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun setNightTempDifference(tempDifference: Int): CustomResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun setHealthCheck(): CustomResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun resetDefaults(): CustomResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun setHeartbeatPeriod(heartbeatPeriod: Int): CustomResult<Unit> {
        TODO("Not yet implemented")
    }
}
