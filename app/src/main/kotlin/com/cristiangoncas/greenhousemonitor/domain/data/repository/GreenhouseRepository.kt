package com.cristiangoncas.greenhousemonitor.domain.data.repository

import com.cristiangoncas.greenhousemonitor.domain.client.ApiClient
import com.cristiangoncas.greenhousemonitor.domain.data.local.GreenhouseDB
import com.cristiangoncas.greenhousemonitor.domain.entity.Event
import com.cristiangoncas.greenhousemonitor.domain.entity.HeartBeat
import com.cristiangoncas.greenhousemonitor.domain.entity.LogEntry
import com.cristiangoncas.greenhousemonitor.domain.entity.RawLogEntry
import com.cristiangoncas.greenhousemonitor.domain.entity.RemoteLogEntry
import com.cristiangoncas.greenhousemonitor.domain.entity.TimestampSerializer
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

interface GreenhouseRepository {

    suspend fun getLogs24h(): List<LogEntry>

    suspend fun getAllLogs(): List<LogEntry>

    suspend fun nextHeartBeat(): HeartBeat

    suspend fun setMaxTemp(maxTemp: Int)

    suspend fun setMinTemp(minTemp: Int)

    suspend fun setMorningTime(morningTime: Int)

    suspend fun setNightTime(nightTime: Int)

    suspend fun setNightTempDifference(tempDifference: Int)

    suspend fun setHealthCheck()

    suspend fun resetDefaults()

    suspend fun setHeartbeatPeriod(heartbeatPeriod: Int)

}

class GreenhouseRepositoryImpl(private val api: ApiClient, private val db: GreenhouseDB) :
    GreenhouseRepository {

    override suspend fun getLogs24h(): List<LogEntry> {
        val logs = api.getLogs24h()
        db.runInTransaction {
            logs.forEach { remoteLogEntry ->
                val rawLogEntry = RawLogEntry.fromRemoteLogEntry(remoteLogEntry)
                db.rawLogEntryDao().insertRawLog(rawLogEntry)
            }
        }
        processRawLogs()
        return db.logEntryDao().fetchAllLogs()
    }

    override suspend fun getAllLogs(): List<LogEntry> {
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

    private fun processRawLogs() {
        db.runInTransaction {
            val rawLogs = db.rawLogEntryDao().fetchAllRawLogs()
            rawLogs.forEach { rawLogEntry ->
                val entry = LogEntry(
                    id = rawLogEntry.id,
                    timestamp = rawLogEntry.time,
                    date = rawLogEntry.time.toReadableDate(),
                    time = rawLogEntry.time.toReadableTime(),
                    data = rawLogEntry.data,
                    event = Event.getEventByValue(rawLogEntry.event)
                )
                db.logEntryDao().insertLogEntry(entry)
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

}