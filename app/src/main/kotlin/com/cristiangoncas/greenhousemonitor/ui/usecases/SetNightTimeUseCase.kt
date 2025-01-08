package com.cristiangoncas.greenhousemonitor.ui.usecases

import com.cristiangoncas.greenhousemonitor.data.local.model.CustomResult
import com.cristiangoncas.greenhousemonitor.data.repository.HeartbeatRepository
import kotlinx.coroutines.flow.Flow

class SetNightTimeUseCase(
    private val heartbeatRepository: HeartbeatRepository
) {

    operator fun invoke(nightTime: Int): Flow<CustomResult<Unit>> {
        return heartbeatRepository.setNightTime(nightTime)
    }
}
