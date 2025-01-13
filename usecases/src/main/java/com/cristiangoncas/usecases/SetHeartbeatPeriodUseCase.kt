package com.cristiangoncas.usecases

import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.data.repository.HeartbeatRepository
import kotlinx.coroutines.flow.Flow

class SetHeartbeatPeriodUseCase(
    private val heartbeatRepository: HeartbeatRepository
) {

    operator fun invoke(heartbeatPeriod: Int): Flow<CustomResult<Unit>> {
        return heartbeatRepository.setHeartbeatPeriod(heartbeatPeriod)
    }
}
