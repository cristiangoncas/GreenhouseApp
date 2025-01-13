package com.cristiangoncas.usecases

import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.data.repository.HeartbeatRepository
import kotlinx.coroutines.flow.Flow

class SetMaxTempUseCase(
    private val heartbeatRepository: HeartbeatRepository
) {

    operator fun invoke(maxTemp: Int): Flow<CustomResult<Unit>> {
            return heartbeatRepository.setMaxTemp(maxTemp)
    }
}