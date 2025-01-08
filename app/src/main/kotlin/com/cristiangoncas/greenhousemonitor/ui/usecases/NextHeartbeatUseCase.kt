package com.cristiangoncas.greenhousemonitor.ui.usecases

import com.cristiangoncas.greenhousemonitor.data.local.model.CustomResult
import com.cristiangoncas.greenhousemonitor.data.local.model.HeartBeat
import com.cristiangoncas.greenhousemonitor.data.repository.HeartbeatRepository
import kotlinx.coroutines.flow.Flow

class NextHeartbeatUseCase(
    private val heartbeatRepository: HeartbeatRepository
) {

    operator fun invoke(): Flow<CustomResult<HeartBeat>> {
        return heartbeatRepository.nextHeartBeat()
    }
}
