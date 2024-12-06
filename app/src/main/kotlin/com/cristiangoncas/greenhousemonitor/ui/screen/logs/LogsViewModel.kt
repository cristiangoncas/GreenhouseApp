package com.cristiangoncas.greenhousemonitor.ui.screen.logs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristiangoncas.greenhousemonitor.domain.data.repository.GreenhouseRepository
import com.cristiangoncas.greenhousemonitor.domain.entity.LogEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LogsViewModel(private val repository: GreenhouseRepository) : ViewModel() {

    private var _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    fun onUiReady() {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { currentState ->
                currentState.copy(loading = true)
            }
            repository.last24hLogs
                .collect {
                    _state.update { currentState ->
                        currentState.copy(logs = it, loading = false)
                    }
                }
        }
    }

    data class UiState(
        var loading: Boolean = true,
        val logs: List<LogEntry> = emptyList(),
    )
}