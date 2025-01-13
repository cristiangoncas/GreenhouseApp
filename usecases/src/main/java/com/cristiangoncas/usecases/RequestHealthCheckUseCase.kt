package com.cristiangoncas.usecases

import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.data.repository.HeartbeatRepository
import kotlinx.coroutines.flow.Flow

class RequestHealthCheckUseCase(
    private val heartbeatRepository: HeartbeatRepository
) {

    operator fun invoke(): Flow<CustomResult<Unit>> {
        return heartbeatRepository.setHealthCheck()
    }
}
