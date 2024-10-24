package com.cristiangoncas.greenhousemonitor.business.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristiangoncas.greenhousemonitor.business.client.ApiClient
import com.cristiangoncas.greenhousemonitor.business.entity.LogEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val apiClient = ApiClient()

     private var _state = MutableStateFlow(UiState())

    val state: StateFlow<UiState> = _state.asStateFlow()

    fun uiReady() {
        viewModelScope.launch {
            val logs = apiClient.getLogs24h()
            _state.value = UiState(logs = logs, loading = false)
        }
    }

    data class UiState(
        var loading: Boolean = true,
        val logs: List<LogEntry> = emptyList(),
    )
}
