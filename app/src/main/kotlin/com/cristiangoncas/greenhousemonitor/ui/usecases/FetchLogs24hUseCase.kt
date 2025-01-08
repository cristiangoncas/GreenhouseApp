package com.cristiangoncas.greenhousemonitor.ui.usecases

import com.cristiangoncas.greenhousemonitor.data.local.model.CustomResult
import com.cristiangoncas.greenhousemonitor.data.local.model.LogEntry
import com.cristiangoncas.greenhousemonitor.data.repository.LogsRepository
import kotlinx.coroutines.flow.Flow

class FetchLogs24hUseCase(
    private val repository: LogsRepository
) {

    operator fun invoke(): Flow<CustomResult<List<LogEntry>>> {
        return repository.last24hLogs
    }
}
