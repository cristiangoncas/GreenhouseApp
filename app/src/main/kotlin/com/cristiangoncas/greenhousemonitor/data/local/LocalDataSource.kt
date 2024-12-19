package com.cristiangoncas.greenhousemonitor.data.local

import com.cristiangoncas.greenhousemonitor.data.local.db.GreenhouseDB
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.TimeZone
import kotlin.jvm.Throws

interface LocalDataSource {

    val last24hLogs: Flow<CustomResult<List<LogEntry>>>

    fun fetchAveragesByPeriodOfTime(period: Long): Flow<CustomResult<AverageTempHumid>>

    fun fetchHeaterEventsByPeriodOfTime(period: Long): Flow<CustomResult<HeaterOnOffCounts>>

    suspend fun processRemoteRawLogs(logs: List<RemoteLogEntry>): Flow<CustomResult<Unit>>
}

class LocalDataSourceImpl(
    private val remoteDataSource: RemoteDataSource,
    private val db: GreenhouseDB,
    private val connectivityState: ConnectivityState
) : LocalDataSource {

    override val last24hLogs: Flow<CustomResult<List<LogEntry>>> =
        db.logEntryDao()
            .fetchLogEntriesLast24hFromPointInTime(
                Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli()
            )
            .onEach { logEntries ->
                if (connectivityState.isConnected.first()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (logEntries.isEmpty()) {
                            fetchRemoteLogs()
                        } else {
                            fetchRemoteLogsFromLastLocalLogId(logEntries)
                        }
                    }
                }
            }
            .distinctUntilChanged()
            .map {
                CustomResult.Success(it)
            }
            .catch {
                CustomResult.Error(it.message ?: "Something went wrong when fetching logs")
            }

    override fun fetchAveragesByPeriodOfTime(period: Long): Flow<CustomResult<AverageTempHumid>> {
        return db.logEntryDao().fetchAverageTempByPeriodOfTime(period)
            .map { CustomResult.Success(it) }
            .catch {
                CustomResult.Error(
                    it.message ?: "Something went wrong when fetching averages by period of time"
                )
            }
    }

    override fun fetchHeaterEventsByPeriodOfTime(period: Long): Flow<CustomResult<HeaterOnOffCounts>> {
        return db.logEntryDao().fetchEventsByPeriodOfTime("heater", period)
            .map { CustomResult.Success(it) }
            .catch {
                CustomResult.Error(
                    it.message
                        ?: "Something went wrong when fetching heater events by period of time"
                )
            }
    }

    override suspend fun processRemoteRawLogs(logs: List<RemoteLogEntry>): Flow<CustomResult<Unit>> {
        try {
            db.runInTransaction {
                // TODO: This business logic will be moved to the backend, sending the last log id and the backend will decide what logs should send back if any.
                val rawLogs = logs.map { RawLogEntry.fromRemoteLogEntry(it) }
                db.rawLogEntryDao().insertRawLogs(rawLogs)
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
                db.logEntryDao().insertLogEntries(entryLogs)
            }
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
}
