package com.cristiangoncas.greenhousemonitor.data.local

import com.cristiangoncas.greenhousemonitor.data.local.db.GreenhouseDB
import com.cristiangoncas.greenhousemonitor.data.local.model.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.data.local.model.CustomResult
import com.cristiangoncas.greenhousemonitor.data.local.model.HeaterOnOffCounts
import com.cristiangoncas.greenhousemonitor.data.local.model.LogEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.temporal.ChronoUnit

interface LocalDataSource {

    val last24hLogs: Flow<CustomResult<List<LogEntry>>>

    fun fetchAveragesByPeriodOfTime(period: Long): Flow<CustomResult<AverageTempHumid>>

    fun fetchHeaterEventsByPeriodOfTime(period: Long): Flow<CustomResult<HeaterOnOffCounts>>

    fun insertLogEntries(logEntries: List<LogEntry>): Flow<CustomResult<Unit>>
}

class LocalDataSourceImpl(
    private val db: GreenhouseDB,
) : LocalDataSource {

    override val last24hLogs: Flow<CustomResult<List<LogEntry>>> =
        db.logEntryDao()
            .fetchLogEntriesLast24hFromPointInTime(
                Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli()
            )
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

    override fun insertLogEntries(logEntries: List<LogEntry>): Flow<CustomResult<Unit>> {
        try {
            db.logEntryDao().insertLogEntries(logEntries)
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
}
