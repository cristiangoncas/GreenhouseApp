package com.cristiangoncas.usecases

import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.data.repository.LogsRepository
import com.cristiangoncas.greenhousemonitor.domain.models.LogEntry
import kotlinx.coroutines.flow.Flow

class FetchLogs24hUseCase(
    private val repository: LogsRepository
) {

    operator fun invoke(): Flow<CustomResult<List<LogEntry>>> {
        return repository.last24hLogs
    }
}
