package com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristiangoncas.greenhousemonitor.BuildConfig
import com.cristiangoncas.greenhousemonitor.business.client.ApiClient
import com.cristiangoncas.greenhousemonitor.business.entity.HeartBeat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeartbeatViewModel : ViewModel() {

    private val apiClient = ApiClient(apiUrl = BuildConfig.API_IP)
    private var _state = MutableStateFlow(UiState())

    val state: StateFlow<UiState> = _state.asStateFlow()

    fun uiReady() {
        viewModelScope.launch {
            val heartBeat = apiClient.nextHeartBeat()
            _state.value =
                UiState(
                    heartBeat = heartBeat,
                    loading = false
                )
        }
    }

    fun setMaxTemp(maxTemp: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val intMaxTemp = maxTemp.toIntOrNull() ?: 0
                if (intMaxTemp in 15..25) {
                    apiClient.setMaxTemp(intMaxTemp)
                    _state.value = _state.value
                        .copy(errors = _state.value.errors - "maxTemp")
                        .copy(heartBeat = _state.value.heartBeat.copy(maxTemp = intMaxTemp.toString()))
                } else {
                    _state.value =
                        UiState(
                            heartBeat = state.value.heartBeat,
                            loading = false,
                            errors = mapOf("maxTemp" to "Temperature out of range, must be between 15 and 25")
                        )
                }
            }
        }
    }

    fun setMinTemp(minTemp: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val intMinTemp = minTemp.toIntOrNull() ?: 0
                if (intMinTemp in 12..18) {
                    apiClient.setMinTemp(intMinTemp)
                    _state.value = _state.value
                        .copy(errors = _state.value.errors - "minTemp")
                        .copy(heartBeat = _state.value.heartBeat.copy(minTemp = intMinTemp.toString()))
                } else {
                    _state.value =
                        UiState(
                            heartBeat = state.value.heartBeat,
                            loading = false,
                            errors = mapOf("minTemp" to "Temperature out of range, must be between 12 and 18")
                        )
                }
            }
        }
    }

    fun setMorningTime(morningTime: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val intMorningTime = morningTime.toIntOrNull() ?: 0
                if (intMorningTime in 5..9) {
                    apiClient.setMorningTime(intMorningTime)
                    _state.value = _state.value
                        .copy(errors = _state.value.errors - "morningTime")
                        .copy(heartBeat = _state.value.heartBeat.copy(morningTime = intMorningTime.toString()))
                } else {
                    _state.value =
                        UiState(
                            heartBeat = state.value.heartBeat,
                            loading = false,
                            errors = mapOf("morningTime" to "Time out of range, must be between 5 and 9")
                        )
                }
            }
        }
    }

    fun setNightTime(nightTime: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val intNightTime = nightTime.toIntOrNull() ?: 0
                if (intNightTime in 4..48) {
                    apiClient.setNightTime(intNightTime)
                    _state.value = _state.value
                        .copy(errors = _state.value.errors - "nightTime")
                        .copy(heartBeat = _state.value.heartBeat.copy(nightTime = intNightTime.toString()))
                } else {
                    _state.value =
                        UiState(
                            heartBeat = state.value.heartBeat,
                            loading = false,
                            errors = mapOf("nightTime" to "Time out of range, must be between 18 and 21")
                        )
                }
            }
        }
    }

    fun setNightTempDifference(nightTempDifference: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val intNightTempDifference = nightTempDifference.toIntOrNull() ?: 0
                if (intNightTempDifference in 1..5) {
                    apiClient.setNightTempDifference(intNightTempDifference)
                    _state.value = _state.value
                        .copy(errors = _state.value.errors - "nightTempDifference")
                        .copy(heartBeat = _state.value.heartBeat.copy(nightTempDifference = intNightTempDifference.toString()))
                } else {
                    _state.value =
                        UiState(
                            heartBeat = state.value.heartBeat,
                            loading = false,
                            errors = mapOf("nightTempDifference" to "Temperature out of range, must be between 1 and 5")
                        )
                }
            }
        }
    }

    fun requestHealthCheck() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    apiClient.setHealthCheck()
                    _state.value = _state.value
                        .copy(errors = _state.value.errors - "setHealthCheck")
                        .copy(heartBeat = HeartBeat())
                } catch (e: Exception) {
                    _state.value =
                        UiState(
                            heartBeat = state.value.heartBeat,
                            loading = false,
                            errors = mapOf("setHealthCheck" to "Something went wrong: ${e.message}")
                        )
                }
            }
        }
    }

    fun resetDefaults() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    apiClient.resetDefaults()
                    _state.value = _state.value
                        .copy(errors = _state.value.errors - "resetDefaults")
                        .copy(heartBeat = HeartBeat())
                } catch (e: Exception) {
                    _state.value =
                        UiState(
                            heartBeat = state.value.heartBeat,
                            loading = false,
                            errors = mapOf("resetDefaults" to "Something went wrong: ${e.message}")
                        )
                }
            }
        }
    }

    fun setHeartbeatPeriod(heartbeatPeriod: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val intHeartbeatPeriod = heartbeatPeriod.toIntOrNull() ?: 0
                if (intHeartbeatPeriod in 10..30) {
                    apiClient.setHeartbeatPeriod(intHeartbeatPeriod)
                    _state.value = _state.value
                        .copy(errors = _state.value.errors - "heartbeatPeriod")
                        .copy(heartBeat = _state.value.heartBeat.copy(heartbeatPeriod = intHeartbeatPeriod.toString()))
                } else {
                    _state.value =
                        UiState(
                            heartBeat = state.value.heartBeat,
                            loading = false,
                            errors = mapOf("heartbeatPeriod" to "Period out of range, must be between 10 and 30")
                        )
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
