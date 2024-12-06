package com.cristiangoncas.greenhousemonitor.data.repository

import com.cristiangoncas.greenhousemonitor.data.local.model.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.data.local.model.HeaterOnOffCounts
import com.cristiangoncas.greenhousemonitor.data.local.model.LogEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach

interface LogsRepository {

    val last24hLogs: Flow<List<LogEntry>>

    val allLogs: Flow<List<LogEntry>>

    fun fetchAveragesByPeriodOfTime(period: Long): Flow<AverageTempHumid>

    fun fetchHeaterEventsByPeriodOfTime(period: Long): Flow<HeaterOnOffCounts>
}

class LogsRepositoryImpl(
    private val localRepository: LocalRepository
) : LogsRepository {

    override val last24hLogs: Flow<List<LogEntry>> = localRepository.last24hLogs.onEach {
        if (it.isEmpty()) {
            localRepository.fetchAndProcessRemoteAllRawLogs()
        }
    }
        .distinctUntilChanged()
    override val allLogs: Flow<List<LogEntry>> = localRepository.allLogs.onEach {
        if (it.isEmpty()) {
            localRepository.fetchAndProcessRemoteAllRawLogs()
        }
    }.distinctUntilChanged()

    override fun fetchAveragesByPeriodOfTime(period: Long): Flow<AverageTempHumid> {
        return localRepository.fetchAveragesByPeriodOfTime(period)
    }

    override fun fetchHeaterEventsByPeriodOfTime(period: Long): Flow<HeaterOnOffCounts> {
        return localRepository.fetchHeaterEventsByPeriodOfTime(period)
    }
}
