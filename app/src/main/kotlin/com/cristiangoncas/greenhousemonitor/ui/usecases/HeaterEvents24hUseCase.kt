package com.cristiangoncas.greenhousemonitor.ui.usecases

import com.cristiangoncas.greenhousemonitor.data.local.model.CustomResult
import com.cristiangoncas.greenhousemonitor.data.local.model.HeaterOnOffCounts
import com.cristiangoncas.greenhousemonitor.data.repository.LogsRepository
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.temporal.ChronoUnit

class HeaterEvents24hUseCase(
    private val logsRepository: LogsRepository
) {

    operator fun invoke(): Flow<CustomResult<HeaterOnOffCounts>> {
        val now = Instant.now()
        val last24h = now.minus(24, ChronoUnit.HOURS)
        return logsRepository.fetchHeaterEventsByPeriodOfTime(last24h.toEpochMilli())
    }
}
