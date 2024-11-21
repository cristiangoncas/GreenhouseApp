package com.cristiangoncas.greenhousemonitor.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cristiangoncas.greenhousemonitor.domain.data.repository.GreenhouseRepository
import com.cristiangoncas.greenhousemonitor.domain.entity.Averages
import com.cristiangoncas.greenhousemonitor.domain.entity.LogEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.temporal.ChronoUnit

class HomeViewModel(private val repository: GreenhouseRepository) : ViewModel() {

    private var _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    fun onUiReady() {
        println("VM: Home - OnUiReady")
        refresh()
    }

    fun refresh() {
        println("VM: Home - Refreshing")
        _state.update { currentState ->
            currentState.copy(loading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val last6h = Instant.now().minus(6, ChronoUnit.HOURS)
            repository.fetchAveragesByPeriodOfTime(last6h.toEpochMilli())
                .collect {
                    _state.update { currentState ->
                        currentState.copy(averages = it, loading = false)
                    }

                }
        }

    }

    data class UiState(
        var loading: Boolean = true,
        val averages: Averages = Averages()
    )
}
