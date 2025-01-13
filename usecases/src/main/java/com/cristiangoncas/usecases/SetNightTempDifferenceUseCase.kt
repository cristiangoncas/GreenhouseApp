package com.cristiangoncas.usecases

import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.data.repository.HeartbeatRepository
import kotlinx.coroutines.flow.Flow

class SetNightTempDifferenceUseCase(
    private val heartbeatRepository: HeartbeatRepository
) {

    operator fun invoke(nightTempDifference: Int): Flow<CustomResult<Unit>> {
        return heartbeatRepository.setNightTempDifference(nightTempDifference)
    }
}
