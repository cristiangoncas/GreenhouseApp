package com.cristiangoncas.usecases

import com.cristiangoncas.greenhousemonitor.domain.models.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.data.repository.LogsRepository
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.temporal.ChronoUnit

class Average48hUseCase(
    private val logsRepository: LogsRepository
) {

    operator fun invoke(): Flow<CustomResult<AverageTempHumid>> {
        val now = Instant.now()
        val last48h = now.minus(48, ChronoUnit.HOURS)
        return logsRepository.fetchAveragesByPeriodOfTime(last48h.toEpochMilli())
    }
}
