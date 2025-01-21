package com.cristiangoncas.usecases

import com.cristiangoncas.greenhousemonitor.domain.models.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.data.repository.LogsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.time.temporal.ChronoUnit

class Average12hUseCase(
    private val logsRepository: LogsRepository
) {

    operator fun invoke(): Flow<CustomResult<AverageTempHumid>> {
        val now = Instant.now()
        val last12h = now.minus(12, ChronoUnit.HOURS)
        return logsRepository.fetchAveragesByPeriodOfTime(last12h.toEpochMilli())
    }
}
