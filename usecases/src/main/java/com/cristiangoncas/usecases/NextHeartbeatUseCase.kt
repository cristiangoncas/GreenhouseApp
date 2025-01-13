package com.cristiangoncas.usecases

import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.greenhousemonitor.domain.models.HeartBeat
import com.cristiangoncas.data.repository.HeartbeatRepository
import kotlinx.coroutines.flow.Flow

class NextHeartbeatUseCase(
    private val heartbeatRepository: HeartbeatRepository
) {

    operator fun invoke(): Flow<CustomResult<HeartBeat>> {
        return heartbeatRepository.nextHeartBeat()
    }
}
