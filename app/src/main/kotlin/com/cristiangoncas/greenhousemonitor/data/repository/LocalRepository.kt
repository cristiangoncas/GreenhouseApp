package com.cristiangoncas.greenhousemonitor.data.repository

import com.cristiangoncas.greenhousemonitor.data.local.db.GreenhouseDB
import com.cristiangoncas.greenhousemonitor.data.local.model.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.data.local.model.HeaterOnOffCounts
import com.cristiangoncas.greenhousemonitor.data.local.model.LogEntry
import com.cristiangoncas.greenhousemonitor.data.local.model.RawLogEntry
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.TimeZone

interface LocalRepository {

    val last24hLogs: Flow<List<LogEntry>>

    val allLogs: Flow<List<LogEntry>>

    fun fetchAveragesByPeriodOfTime(period: Long): Flow<AverageTempHumid>

    fun fetchHeaterEventsByPeriodOfTime(period: Long): Flow<HeaterOnOffCounts>

    suspend fun fetchAndProcessRemoteAllRawLogs()
}

class LocalRepositoryImpl(
    private val remoteRepository: RemoteRepository,
    private val db: GreenhouseDB
) : LocalRepository {

    override val last24hLogs: Flow<List<LogEntry>> =
        db.logEntryDao().fetchLogEntriesLast24hFromPointInTime(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli())

    override val allLogs: Flow<List<LogEntry>> = db.logEntryDao().fetchAllLogs()

    override fun fetchAveragesByPeriodOfTime(period: Long): Flow<AverageTempHumid> {
        return db.logEntryDao().fetchAverageTempByPeriodOfTime(period)
    }

    override fun fetchHeaterEventsByPeriodOfTime(period: Long): Flow<HeaterOnOffCounts> {
        return db.logEntryDao().fetchEventsByPeriodOfTime(period)
    }

    override suspend fun fetchAndProcessRemoteAllRawLogs() {
        try {
            // TODO: Add batching to avoid having huge data sets. 10 days worth of logs is around 500 logs.
            val logs = remoteRepository.getAllLogs()
            db.runInTransaction {
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
        } catch (e: Exception) {
            // TODO: Add error handling and propagate that to the UI
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
