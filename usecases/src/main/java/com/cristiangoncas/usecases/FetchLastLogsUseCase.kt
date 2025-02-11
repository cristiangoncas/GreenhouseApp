package com.cristiangoncas.usecases

import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.data.repository.LogsRepository
import com.cristiangoncas.greenhousemonitor.domain.models.LogEntry
import kotlinx.coroutines.flow.Flow

class FetchLastLogsUseCase(
    private val repository: LogsRepository
) {

    operator fun invoke(): Flow<CustomResult<List<LogEntry>>> {
        return repository.fetchLastLogEntries()
    }
}
