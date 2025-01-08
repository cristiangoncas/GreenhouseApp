package com.cristiangoncas.greenhousemonitor.ui.usecases

import com.cristiangoncas.greenhousemonitor.data.local.model.CustomResult
import com.cristiangoncas.greenhousemonitor.data.repository.HeartbeatRepository
import kotlinx.coroutines.flow.Flow

class SetNightTempDifferenceUseCase(
    private val heartbeatRepository: HeartbeatRepository
) {

    operator fun invoke(nightTempDifference: Int): Flow<CustomResult<Unit>> {
        return heartbeatRepository.setNightTempDifference(nightTempDifference)
    }
}
