package com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristiangoncas.greenhousemonitor.data.local.model.CustomResult
import com.cristiangoncas.greenhousemonitor.data.local.model.HeartBeat
import com.cristiangoncas.greenhousemonitor.data.repository.HeartbeatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeartbeatViewModel(private val heartbeatRepository: HeartbeatRepository) : ViewModel() {

    private val uiActions = MutableSharedFlow<(UiState) -> UiState>()
    val state: StateFlow<UiState> = merge(
        heartbeatRepository.nextHeartBeat()
            .map { heartBeat ->
                { currentState ->
                    heartBeat as CustomResult.Success
                    currentState.copy(
                        heartBeat = heartBeat.data,
                        loading = false
                    )
                }
            },
        uiActions
    )
        .scan(UiState(loading = true)) { currentState, reducer ->
            reducer(currentState)
        }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = UiState(loading = true)
        )

    fun setMaxTemp(maxTemp: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val intMaxTemp = maxTemp.toIntOrNull() ?: 0
                if (intMaxTemp in 15..25) {
                    heartbeatRepository.setMaxTemp(intMaxTemp)
                    // TODO: All this actions require error handling
                    uiActions.emit { currentState ->
                        currentState.copy(
                            errors = currentState.errors - "maxTemp",
                            heartBeat = currentState.heartBeat.copy(maxTemp = intMaxTemp.toString())
                        )
                    }
                } else {
                    uiActions.emit { currentState ->
                        currentState.copy(
                            heartBeat = state.value.heartBeat,
                            loading = false,
                            errors = mapOf("maxTemp" to "Temperature out of range, must be between 15 and 25")
                        )
                    }
                }
            }
        }
    }

    fun setMinTemp(minTemp: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val intMinTemp = minTemp.toIntOrNull() ?: 0
                if (intMinTemp in 12..18) {
                    heartbeatRepository.setMinTemp(intMinTemp)
                    uiActions.emit { currentState ->
                        currentState.copy(
                            errors = currentState.errors - "minTemp",
                            heartBeat = currentState.heartBeat.copy(minTemp = intMinTemp.toString())
                        )
                    }
                } else {
                    uiActions.emit { currentState ->
                        currentState.copy(
                            heartBeat = state.value.heartBeat,
                            loading = false,
                            errors = mapOf("minTemp" to "Temperature out of range, must be between 12 and 18")
                        )
                    }
                }
            }
        }
    }

    fun setMorningTime(morningTime: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val intMorningTime = morningTime.toIntOrNull() ?: 0
                if (intMorningTime in 5..9) {
                    heartbeatRepository.setMorningTime(intMorningTime)
                    uiActions.emit { currentState ->
                        currentState.copy(
                            errors = currentState.errors - "morningTime",
                            heartBeat = currentState.heartBeat.copy(morningTime = intMorningTime.toString())
                        )
                    }
                } else {
                    uiActions.emit { currentState ->
                        currentState.copy(
                            heartBeat = state.value.heartBeat,
                            loading = false,
                            errors = mapOf("morningTime" to "Time out of range, must be between 5 and 9")
                        )
                    }
                }
            }
        }
    }

    fun setNightTime(nightTime: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val intNightTime = nightTime.toIntOrNull() ?: 0
                if (intNightTime in 4..48) {
                    heartbeatRepository.setNightTime(intNightTime)
                    uiActions.emit { currentState ->
                        currentState.copy(
                            errors = currentState.errors - "nightTime",
                            heartBeat = currentState.heartBeat.copy(nightTime = intNightTime.toString())
                        )
                    }
                } else {
                    uiActions.emit { currentState ->
                        currentState.copy(
                            heartBeat = state.value.heartBeat,
                            loading = false,
                            errors = mapOf("nightTime" to "Time out of range, must be between 18 and 21")
                        )
                    }
                }
            }
        }
    }

    fun setNightTempDifference(nightTempDifference: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val intNightTempDifference = nightTempDifference.toIntOrNull() ?: 0
                if (intNightTempDifference in 1..5) {
                    heartbeatRepository.setNightTempDifference(intNightTempDifference)
                    uiActions.emit { currentState ->
                        currentState.copy(
                            errors = currentState.errors - "nightTempDifference",
                            heartBeat = currentState.heartBeat.copy(nightTempDifference = intNightTempDifference.toString())
                        )
                    }
                } else {
                    uiActions.emit { currentState ->
                        currentState.copy(
                            heartBeat = state.value.heartBeat,
                            loading = false,
                            errors = mapOf("nightTempDifference" to "Temperature out of range, must be between 1 and 5")
                        )
                    }
                }
            }
        }
    }

    fun requestHealthCheck() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    heartbeatRepository.setHealthCheck()
                    uiActions.emit { currentState ->
                        currentState.copy(
                            errors = currentState.errors - "setHealthCheck",
                            heartBeat = HeartBeat()
                        )
                    }
                } catch (e: Exception) {
                    uiActions.emit { currentState ->
                        currentState.copy(
                            heartBeat = state.value.heartBeat,
                            loading = false,
                            errors = mapOf("setHealthCheck" to "Something went wrong: ${e.message}")
                        )
                    }
                }
            }
        }
    }

    fun resetDefaults() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    heartbeatRepository.resetDefaults()
                    uiActions.emit { currentState ->
                        currentState.copy(
                            errors = currentState.errors - "resetDefaults",
                            heartBeat = HeartBeat()
                        )
                    }
                } catch (e: Exception) {
                    uiActions.emit { currentState ->
                        currentState.copy(
                            heartBeat = state.value.heartBeat,
                            loading = false,
                            errors = mapOf("resetDefaults" to "Something went wrong: ${e.message}")
                        )
                    }
                }
            }
        }
    }

    fun setHeartbeatPeriod(heartbeatPeriod: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val intHeartbeatPeriod = heartbeatPeriod.toIntOrNull() ?: 0
                if (intHeartbeatPeriod in 10..30) {
                    heartbeatRepository.setHeartbeatPeriod(intHeartbeatPeriod)
                    uiActions.emit { currentState ->
                        currentState.copy(
                            errors = currentState.errors - "heartbeatPeriod",
                            heartBeat = currentState.heartBeat.copy(
                                heartbeatPeriod =
                                intHeartbeatPeriod.toString()
                            )
                        )
                    }
                } else {
                    uiActions.emit { currentState ->
                        currentState.copy(
                            heartBeat = state.value.heartBeat,
                            loading = false,
                            errors = mapOf("heartbeatPeriod" to "Period out of range, must be between 10 and 30")
                        )
                    }
                }
            }
        }
    }

    data class UiState(
        var loading: Boolean = true,
        val heartBeat: HeartBeat = HeartBeat(),
        val errors: Map<String, String> = mapOf()
    )
}
