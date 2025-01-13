package com.cristiangoncas.usecases

import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.data.repository.HeartbeatRepository
import kotlinx.coroutines.flow.Flow

class SetMorningTimeUseCase(
    private val heartbeatRepository: HeartbeatRepository
) {

    operator fun invoke(morningTime: Int): Flow<CustomResult<Unit>> {
        return heartbeatRepository.setMorningTime(morningTime)
    }
}
