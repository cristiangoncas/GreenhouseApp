package com.cristiangoncas.usecases

import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.greenhousemonitor.domain.models.HeaterOnOffCounts
import com.cristiangoncas.data.repository.LogsRepository
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
