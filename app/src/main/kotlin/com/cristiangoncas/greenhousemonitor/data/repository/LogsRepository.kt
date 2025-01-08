package com.cristiangoncas.greenhousemonitor.data.repository

import com.cristiangoncas.greenhousemonitor.data.local.LocalDataSource
import com.cristiangoncas.greenhousemonitor.data.local.model.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.data.local.model.CustomResult
import com.cristiangoncas.greenhousemonitor.data.local.model.HeaterOnOffCounts
import com.cristiangoncas.greenhousemonitor.data.local.model.LogEntry
import com.cristiangoncas.greenhousemonitor.data.local.model.RawLogEntry
import com.cristiangoncas.greenhousemonitor.data.remote.RemoteDataSource
import com.cristiangoncas.greenhousemonitor.data.remote.model.RemoteLogEntry
import com.cristiangoncas.greenhousemonitor.ui.common.ConnectivityState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

interface LogsRepository {

    val last24hLogs: Flow<CustomResult<List<LogEntry>>>

    fun fetchAveragesByPeriodOfTime(period: Long): Flow<CustomResult<AverageTempHumid>>

    fun fetchHeaterEventsByPeriodOfTime(period: Long): Flow<CustomResult<HeaterOnOffCounts>>
}

class LogsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val connectivityState: ConnectivityState
) : LogsRepository {

    override val last24hLogs: Flow<CustomResult<List<LogEntry>>> =
        localDataSource.last24hLogs
            .onEach { logEntries ->
                if (logEntries.isSuccessful()) {
                    logEntries as CustomResult.Success
                    if (connectivityState.isConnected.first()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (logEntries.data.isEmpty()) {
                                fetchRemoteLogs()
                            } else {
                                fetchRemoteLogsFromLastLocalLogId(logEntries.data)
                            }
                        }
                    }
                }
            }
            .distinctUntilChanged()
            .catch {
                CustomResult.Error(it.message ?: "Something went wrong when fetching logs")
            }

    override fun fetchAveragesByPeriodOfTime(period: Long): Flow<CustomResult<AverageTempHumid>> {
        return localDataSource.fetchAveragesByPeriodOfTime(period)
    }

    override fun fetchHeaterEventsByPeriodOfTime(period: Long): Flow<CustomResult<HeaterOnOffCounts>> {
        return localDataSource.fetchHeaterEventsByPeriodOfTime(period)
    }

    private fun processRemoteRawLogs(logs: List<RemoteLogEntry>): Flow<CustomResult<Unit>> {
        try {
            // TODO: This business logic will be moved to the backend, sending the last log id and the backend will decide what logs should send back if any.
            val rawLogs = logs.map { RawLogEntry.fromRemoteLogEntry(it) }
            val entryLogs = rawLogs.map { rawLogEntry ->
                LogEntry(
                    id = rawLogEntry.id,
                    timestamp = rawLogEntry.time,
                    date = rawLogEntry.time.toReadableDate(),
                    time = rawLogEntry.time.toReadableTime(),
                    data = rawLogEntry.data,
                    event = rawLogEntry.event
                )
            }
            localDataSource.insertLogEntries(entryLogs)
            return flow {
                emit(CustomResult.Success(Unit))
            }
        } catch (e: Exception) {
            return flow {
                emit(
                    CustomResult.Error(
                        e.message ?: "Something went wrong when processing remote logs"
                    )
                )
            }
        }
    }

    @Throws
    private suspend fun fetchRemoteLogs() {
        val allLogsResult = remoteDataSource.getAllLogs()
        if (allLogsResult.isSuccessful()) {
            // TODO: Add batching to avoid having huge data sets. 10 days worth of logs is around 500 logs.
            allLogsResult as CustomResult.Success
            processRemoteRawLogs(allLogsResult.data)
        } else {
            allLogsResult as CustomResult.Error
            throw Exception(allLogsResult.message)
        }
    }

    @Throws
    private suspend fun fetchRemoteLogsFromLastLocalLogId(logEntries: List<LogEntry>) {
        val lastLogResult = remoteDataSource.getLastLog()
        if (lastLogResult.isSuccessful()) {
            lastLogResult as CustomResult.Success
            val lastLogId = lastLogResult.data.id

            if (logEntries.first().id != lastLogId) {
                val logsResult = remoteDataSource.getLogs24h()
                if (logsResult.isSuccessful()) {
                    logsResult as CustomResult.Success
                    processRemoteRawLogs(logsResult.data)
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

    private fun Long.toReadableDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
        return dateFormat.format(this)
    }

    private fun Long.toReadableTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
        return dateFormat.format(this)
    }
}
