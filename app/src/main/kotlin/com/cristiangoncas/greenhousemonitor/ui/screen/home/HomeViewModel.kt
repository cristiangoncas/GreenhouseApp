package com.cristiangoncas.greenhousemonitor.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristiangoncas.greenhousemonitor.BuildConfig
import com.cristiangoncas.greenhousemonitor.domain.client.Api
import com.cristiangoncas.greenhousemonitor.domain.client.ApiClient
import com.cristiangoncas.greenhousemonitor.domain.data.local.GreenhouseDB
import com.cristiangoncas.greenhousemonitor.domain.data.repository.GreenhouseRepository
import com.cristiangoncas.greenhousemonitor.domain.data.repository.GreenhouseRepositoryImpl
import com.cristiangoncas.greenhousemonitor.domain.entity.LogEntry
import com.cristiangoncas.greenhousemonitor.domain.entity.RemoteLogEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class HomeViewModel(private val repository: GreenhouseRepository) : ViewModel() {

    private var _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun uiReady() {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = UiState(logs = state.value.logs, loading = true)
            val logs = repository.getLogs24h()
            _state.value = UiState(logs = logs, loading = false)
        }
    }

    data class UiState(
        var loading: Boolean = true,
        val logs: List<LogEntry> = emptyList(),
    )
}
