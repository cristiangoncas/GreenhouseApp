package com.cristiangoncas.usecases

import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.data.repository.HeartbeatRepository
import kotlinx.coroutines.flow.Flow

class SetMinTempUseCase(
    private val heartbeatRepository: HeartbeatRepository
) {
    operator fun invoke(minTemp: Int): Flow<CustomResult<Unit>> {
        return heartbeatRepository.setMinTemp(minTemp)
    }
}
