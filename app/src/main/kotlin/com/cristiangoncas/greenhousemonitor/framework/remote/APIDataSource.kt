package com.cristiangoncas.greenhousemonitor.framework.remote

import com.cristiangoncas.data.remote.RemoteDataSource
import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.greenhousemonitor.domain.models.HeartBeat
import com.cristiangoncas.greenhousemonitor.domain.models.LogEntry
import com.cristiangoncas.greenhousemonitor.framework.remote.model.RemoteLogEntry
import com.cristiangoncas.greenhousemonitor.framework.remote.client.ApiImpl
import com.cristiangoncas.greenhousemonitor.framework.remote.model.RemoteHeartBeat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class APIDataSource(private val api: ApiImpl) : RemoteDataSource {

    override suspend fun getLastLog(): CustomResult<LogEntry> {
        val lastLog = api.getLastLog()
        if (lastLog.isSuccessful()) {
            lastLog as CustomResult.Success
            return CustomResult.Success(lastLog.data.toDomainModel())
        } else {
            return lastLog as CustomResult.Error
        }
    }

    override suspend fun getLogs24h(): CustomResult<List<LogEntry>> {
        val logs24h = api.getLogs24h()
        if (logs24h.isSuccessful()) {
            logs24h as CustomResult.Success
            return CustomResult.Success(logs24h.data.map { it.toDomainModel() })
        } else {
            return logs24h as CustomResult.Error
        }

    }

    override suspend fun getAllLogs(): CustomResult<List<LogEntry>> {
        val allLogs = api.getAllLogs()
        if (allLogs.isSuccessful()) {
            allLogs as CustomResult.Success
            return CustomResult.Success(allLogs.data.map { it.toDomainModel() })
        } else {
            return allLogs as CustomResult.Error
        }
    }

    override suspend fun nextHeartBeat(): CustomResult<HeartBeat> {
        val nextHeartBeat = api.nextHeartBeat()
        if (nextHeartBeat.isSuccessful()) {
            nextHeartBeat as CustomResult.Success
            return CustomResult.Success(nextHeartBeat.data.toDomainModel())
        } else {
            return nextHeartBeat as CustomResult.Error
        }
    }

    override suspend fun setMaxTemp(maxTemp: Int): CustomResult<Unit> {
        return api.setMaxTemp(maxTemp)
    }

    override suspend fun setMinTemp(minTemp: Int): CustomResult<Unit> {
        return api.setMinTemp(minTemp)
    }

    override suspend fun setMorningTime(morningTime: Int): CustomResult<Unit> {
        return api.setMorningTime(morningTime)
    }

    override suspend fun setNightTime(nightTime: Int): CustomResult<Unit> {
        return api.setNightTime(nightTime)
    }

    override suspend fun setNightTempDifference(tempDifference: Int): CustomResult<Unit> {
        return api.setNightTempDifference(tempDifference)
    }

    override suspend fun setHealthCheck(): CustomResult<Unit> {
        return api.setHealthCheck()
    }

    override suspend fun resetDefaults(): CustomResult<Unit> {
        return api.resetDefaults()
    }

    override suspend fun setHeartbeatPeriod(heartbeatPeriod: Int): CustomResult<Unit> {
        return api.setHeartbeatPeriod(heartbeatPeriod)
    }
}

private fun RemoteLogEntry.toDomainModel(): LogEntry {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }
    val date = dateFormat.parse(time)
        ?: throw IllegalArgumentException("Invalid date format")

    return LogEntry(
        id = id,
        timestamp = date.time,
        date = date.time.toReadableDate(),
        time = date.time.toReadableTime(),
        data = data,
        event = event
    )
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

private fun RemoteHeartBeat.toDomainModel(): HeartBeat {
    return HeartBeat(
        maxTemp = maxTemp,
        minTemp = minTemp,
        morningTime = morningTime,
        nightTime = nightTime,
        nightTempDifference = nightTempDifference,
        resetDefaults = resetDefaults,
        heartbeatPeriod = heartbeatPeriod
    )
}
