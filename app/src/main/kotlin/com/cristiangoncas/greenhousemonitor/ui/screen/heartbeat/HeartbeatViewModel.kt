package com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristiangoncas.greenhousemonitor.BuildConfig
import com.cristiangoncas.greenhousemonitor.business.client.ApiClient
import com.cristiangoncas.greenhousemonitor.business.entity.HeartBeat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    data class UiState(
        var loading: Boolean = true,
        val heartBeat: HeartBeat = HeartBeat(emptyMap()),
    )
}
