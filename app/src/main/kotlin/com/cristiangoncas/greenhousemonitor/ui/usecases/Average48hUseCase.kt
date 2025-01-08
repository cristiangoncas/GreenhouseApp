package com.cristiangoncas.greenhousemonitor.ui.usecases

import com.cristiangoncas.greenhousemonitor.data.local.model.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.data.local.model.CustomResult
import com.cristiangoncas.greenhousemonitor.data.repository.LogsRepository
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
