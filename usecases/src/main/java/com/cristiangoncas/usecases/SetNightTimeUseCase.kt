package com.cristiangoncas.usecases

import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.data.repository.HeartbeatRepository
import kotlinx.coroutines.flow.Flow

class SetNightTimeUseCase(
    private val heartbeatRepository: HeartbeatRepository
) {

    operator fun invoke(nightTime: Int): Flow<CustomResult<Unit>> {
        return heartbeatRepository.setNightTime(nightTime)
    }
}
