package com.cristiangoncas.greenhousemonitor.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristiangoncas.greenhousemonitor.domain.data.repository.GreenhouseRepository
import com.cristiangoncas.greenhousemonitor.domain.entity.Averages
import com.cristiangoncas.greenhousemonitor.domain.entity.LogEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit

class HomeViewModel(private val repository: GreenhouseRepository) : ViewModel() {

    private var _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun uiReady() {
        // TODO: If the app is opened for the first time:
        //  - first fetch logs for the last 24h
        //  - In parallel fetch all logs
        viewModelScope.launch(Dispatchers.IO) {
            refresh()
        }
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = UiState(loading = true).copy(logs = state.value.logs)
            val last6h = Instant.now().minus(6, ChronoUnit.HOURS)
            repository.fetchAveragesByPeriodOfTime(last6h.toEpochMilli())
                .map {
                    UiState(averages = it, loading = false)
                }
                .collect {
                    _state.value = _state.value.copy(averages = it.averages)
                }
        }
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = UiState(loading = true).copy(logs = state.value.logs)
            repository.getLogs24h()
                .map {
                    UiState(logs = it, loading = false)
                }
                .collect {
                    _state.value = it
                }
        }
    }

    data class UiState(
        var loading: Boolean = true,
        val averages: Averages = Averages(),
        val logs: List<LogEntry> = emptyList(),
    )
}
