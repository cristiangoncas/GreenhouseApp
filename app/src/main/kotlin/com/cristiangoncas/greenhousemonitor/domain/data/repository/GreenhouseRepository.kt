package com.cristiangoncas.greenhousemonitor.domain.data.repository

import com.cristiangoncas.greenhousemonitor.domain.client.ApiClient
import com.cristiangoncas.greenhousemonitor.domain.data.local.GreenhouseDB
import com.cristiangoncas.greenhousemonitor.domain.entity.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.domain.entity.HeartBeat
import com.cristiangoncas.greenhousemonitor.domain.entity.HeaterOnOffCounts
import com.cristiangoncas.greenhousemonitor.domain.entity.LogEntry
import com.cristiangoncas.greenhousemonitor.domain.entity.RawLogEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.TimeZone

interface GreenhouseRepository {

    val last24hLogs: Flow<List<LogEntry>>

//    suspend fun getLogs24h(): Flow<List<LogEntry>>

    suspend fun getAllLogs(): Flow<List<LogEntry>>

    suspend fun nextHeartBeat(): HeartBeat

    suspend fun setMaxTemp(maxTemp: Int)

    suspend fun setMinTemp(minTemp: Int)

    suspend fun setMorningTime(morningTime: Int)

    suspend fun setNightTime(nightTime: Int)

    suspend fun setNightTempDifference(tempDifference: Int)

    suspend fun setHealthCheck()

    suspend fun resetDefaults()

    suspend fun setHeartbeatPeriod(heartbeatPeriod: Int)

    suspend fun fetchAveragesByPeriodOfTime(period: Long): Flow<AverageTempHumid>

    suspend fun fetchHeaterEventsByPeriodOfTime(period: Long): Flow<HeaterOnOffCounts>

}

class GreenhouseRepositoryImpl(private val api: ApiClient, private val db: GreenhouseDB) :
    GreenhouseRepository {

    override val last24hLogs: Flow<List<LogEntry>> = db.logEntryDao().fetchLogEntriesLast24hFromPointInTime(
        Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli())
        .onEach {
            if (it.isEmpty()) {
                fetchAndProcessRemoteAllRawLogs()
            }
        }
        .distinctUntilChanged()

//    override suspend fun getLogs24h(): Flow<List<LogEntry>> {
//        val last24h =  Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli()
//        CoroutineScope(Dispatchers.IO).launch {
//            if (db.logEntryDao().availableLogsOlderThan24h(last24h) == 0) {
//                fetchAndProcessRemoteAllRawLogs()
//            }
//        }
//        val logs = db.logEntryDao().fetchLogEntriesLast24hFromPointInTime(last24h)
//            .onEach {
//                if (it.isEmpty()) {
//                    fetchAndProcessRemoteAllRawLogs()
//                }
//            }
//            .distinctUntilChanged()
//        return logs
//    }

    override suspend fun getAllLogs(): Flow<List<LogEntry>> {
        TODO("Not yet implemented")
//        return api.getAllLogs()
    }

    override suspend fun nextHeartBeat(): HeartBeat {
        return api.nextHeartBeat()
    }

    override suspend fun setMaxTemp(maxTemp: Int) {
        api.setMaxTemp(maxTemp)
    }

    override suspend fun setMinTemp(minTemp: Int) {
        api.setMinTemp(minTemp)
    }

    override suspend fun setMorningTime(morningTime: Int) {
        api.setMorningTime(morningTime)
    }

    override suspend fun setNightTime(nightTime: Int) {
        api.setNightTime(nightTime)
    }

    override suspend fun setNightTempDifference(tempDifference: Int) {
        api.setNightTempDifference(tempDifference)
    }

    override suspend fun setHealthCheck() {
        api.setHealthCheck()
    }

    override suspend fun resetDefaults() {
        api.resetDefaults()
    }

    override suspend fun setHeartbeatPeriod(heartbeatPeriod: Int) {
        api.setHeartbeatPeriod(heartbeatPeriod)
    }

    override suspend fun fetchAveragesByPeriodOfTime(period: Long): Flow<AverageTempHumid> {
        return db.logEntryDao().fetchAverageTempByPeriodOfTime(period)
    }

    override suspend fun fetchHeaterEventsByPeriodOfTime(period: Long): Flow<HeaterOnOffCounts> {
        return db.logEntryDao().fetchEventsByPeriodOfTime(period)
    }

    private suspend fun fetchAndProcessRemoteAllRawLogs() {
        try {
            // TODO: Add batching to avoid having huge data sets. 10 days worth of logs is around 500 logs.
            val logs = api.getAllLogs()
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