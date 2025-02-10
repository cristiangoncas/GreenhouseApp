package com.cristiangoncas.data.repository

import com.cristiangoncas.data.local.LocalDataSource
import com.cristiangoncas.data.remote.ConnectivityDataSource
import com.cristiangoncas.data.remote.RemoteDataSource
import com.cristiangoncas.greenhousemonitor.domain.models.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.greenhousemonitor.domain.models.HeaterOnOffCounts
import com.cristiangoncas.greenhousemonitor.domain.models.LogEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

interface LogsRepository {

    fun fetchLastLogEntries(): Flow<CustomResult<List<LogEntry>>>

    fun fetchAveragesByPeriodOfTime(period: Long): Flow<CustomResult<AverageTempHumid>>

    fun fetchHeaterEventsByPeriodOfTime(period: Long): Flow<CustomResult<HeaterOnOffCounts>>
}

class LogsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val connectivityState: ConnectivityDataSource,
    private val refreshDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LogsRepository {


    override fun fetchLastLogEntries(): Flow<CustomResult<List<LogEntry>>> {
        return localDataSource.fetchLastLogEntries()
            .onStart {
                CoroutineScope(refreshDispatcher).launch { refreshIfNecessary() }
            }
            .distinctUntilChanged()
            .catch {
                CustomResult.Error(it.message ?: "Something went wrong when fetching logs")
            }
    }

    private suspend fun refreshIfNecessary() {
        // TODO: Fetch last log id, send that to an API and server would return what logs are missing.
//        val logEntries = localDataSource.fetchLastLogEntries()
        if (connectivityState.isConnected.first()) {
//            logEntries as CustomResult.Success
//            if (logEntries.data.isEmpty()) {
            fetchRemoteLogs()
//            } else {
//                fetchRemoteLogsFromLastLocalLogId(logEntries.data)
//            }
        }
    }

    override fun fetchAveragesByPeriodOfTime(period: Long): Flow<CustomResult<AverageTempHumid>> {
        return localDataSource.fetchAveragesByPeriodOfTime(period)
    }

    override fun fetchHeaterEventsByPeriodOfTime(period: Long): Flow<CustomResult<HeaterOnOffCounts>> {
        return localDataSource.fetchHeaterEventsByPeriodOfTime(period)
    }

    @Throws
    private suspend fun fetchRemoteLogs() {
        val allLogsResult = remoteDataSource.getAllLogs()
        if (allLogsResult.isSuccessful()) {
            // TODO: Add batching to avoid having huge data sets. 10 days worth of logs is around 500 logs.
            allLogsResult as CustomResult.Success
            allLogsResult.data.forEach {

                println("Result: ${it.data}")
            }
            val persistedLogs = persistLogs(allLogsResult.data).first()
            if (!persistedLogs.isSuccessful()) {
                persistedLogs as CustomResult.Error
                throw Exception(persistedLogs.message)
            }
        } else {
            allLogsResult as CustomResult.Error
            throw Exception(allLogsResult.message)
        }
    }

    @Throws
    private suspend fun fetchRemoteLogsFromLastLocalLogId(logEntries: List<LogEntry>) {
        // TODO: Move this logic to the back end.
        //  Android should pass the last id and the back end should return the logs from that id.
        val lastLogResult = remoteDataSource.getLastLog()
        if (lastLogResult.isSuccessful()) {
            lastLogResult as CustomResult.Success
            val lastLogId = lastLogResult.data.id

            if (logEntries.first().id != lastLogId) {
                val logsResult = remoteDataSource.getLogs24h()
                if (logsResult.isSuccessful()) {
                    logsResult as CustomResult.Success
                    persistLogs(logsResult.data)
                } else {
                    logsResult as CustomResult.Error
                    throw Exception(logsResult.message)
                }
            }
        } else {
            lastLogResult as CustomResult.Error
            throw Exception(lastLogResult.message)
        }
    }

    private fun persistLogs(logEntries: List<LogEntry>): Flow<CustomResult<Unit>> {
        return localDataSource.insertLogEntries(logEntries)
    }
}
