package com.cristiangoncas.greenhousemonitor.framework.local

import com.cristiangoncas.data.local.LocalDataSource
import com.cristiangoncas.greenhousemonitor.framework.local.database.GreenhouseDB
import com.cristiangoncas.greenhousemonitor.framework.local.model.DbLogEntry
import com.cristiangoncas.greenhousemonitor.domain.models.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.greenhousemonitor.domain.models.HeaterOnOffCounts
import com.cristiangoncas.greenhousemonitor.domain.models.LogEntry
import com.cristiangoncas.greenhousemonitor.framework.local.database.LogEntryDao
import com.cristiangoncas.greenhousemonitor.framework.local.model.DbAverageTempHumid
import com.cristiangoncas.greenhousemonitor.framework.local.model.DbHeaterOnOffCounts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.temporal.ChronoUnit

class RoomDataSource(
    private val logEntryDao: LogEntryDao,
) : LocalDataSource {

    override fun fetchLastLogEntries(): Flow<CustomResult<List<LogEntry>>> {
        return logEntryDao
            .fetchLatestLogEntriesFlow()
            .map { entries ->
                println("Entries: $entries")
                val mappedEntries = entries
                    .map { it.toDomainModel() }
                CustomResult.Success(mappedEntries)
            }
            .catch {
                CustomResult.Error(it.message ?: "Something went wrong when fetching logs")
            }
    }

    override fun fetchAveragesByPeriodOfTime(period: Long): Flow<CustomResult<AverageTempHumid>> {
        return logEntryDao.fetchAverageTempByPeriodOfTime(period)
            .map { CustomResult.Success(it.toDomainModel()) }
            .catch {
                CustomResult.Error(
                    it.message ?: "Something went wrong when fetching averages by period of time"
                )
            }
    }

    override fun fetchHeaterEventsByPeriodOfTime(period: Long): Flow<CustomResult<HeaterOnOffCounts>> {
        return logEntryDao.fetchEventsByPeriodOfTime("heater", period)
            .map { CustomResult.Success(it.toDomainModel()) }
            .catch {
                CustomResult.Error(
                    it.message
                        ?: "Something went wrong when fetching heater events by period of time"
                )
            }
    }

    override fun insertLogEntries(logEntries: List<LogEntry>): Flow<CustomResult<Unit>> {
        try {
            val dbLogEntries = logEntries.map { it.toFrameworkModel() }
            logEntryDao.insertLogEntries(dbLogEntries)
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

    private fun DbLogEntry.toDomainModel(): LogEntry {
        return LogEntry(
            id = id,
            timestamp = timestamp,
            date = date,
            time = time,
            data = data,
            event = event
        )
    }

    private fun LogEntry.toFrameworkModel(): DbLogEntry {
        return DbLogEntry(
            id = id,
            timestamp = timestamp,
            date = date,
            time = time,
            data = data,
            event = event
        )
    }

    private fun DbAverageTempHumid.toDomainModel(): AverageTempHumid {
        return AverageTempHumid(
            avgTempRead = avgTempRead,
            avgHumidRead = avgHumidRead
        )
    }

    private fun DbHeaterOnOffCounts.toDomainModel(): HeaterOnOffCounts {
        return HeaterOnOffCounts(
            heaterOnCount = heaterOnCount,
            heaterOffCount = heaterOffCount
        )
    }
}
