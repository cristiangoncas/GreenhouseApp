package com.cristiangoncas.data.local

import com.cristiangoncas.greenhousemonitor.domain.models.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.greenhousemonitor.domain.models.HeaterOnOffCounts
import com.cristiangoncas.greenhousemonitor.domain.models.LogEntry
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    val last24hLogs: Flow<CustomResult<List<LogEntry>>>

    fun fetchAveragesByPeriodOfTime(period: Long): Flow<CustomResult<AverageTempHumid>>

    fun fetchHeaterEventsByPeriodOfTime(period: Long): Flow<CustomResult<HeaterOnOffCounts>>

    fun insertLogEntries(logEntries: List<LogEntry>): Flow<CustomResult<Unit>>
}
