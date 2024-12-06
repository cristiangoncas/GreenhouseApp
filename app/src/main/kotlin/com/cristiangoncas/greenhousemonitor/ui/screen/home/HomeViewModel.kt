package com.cristiangoncas.greenhousemonitor.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristiangoncas.greenhousemonitor.data.local.model.Average
import com.cristiangoncas.greenhousemonitor.data.local.model.EventCount
import com.cristiangoncas.greenhousemonitor.data.repository.LogsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit

class HomeViewModel(private val logsRepository: LogsRepository) : ViewModel() {

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 0)
    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<UiState> = refreshTrigger
        .onStart { emit(Unit) }
        .flatMapLatest {
            val now = Instant.now()
            val last12h = now.minus(12, ChronoUnit.HOURS)
            val last24h = now.minus(24, ChronoUnit.HOURS)
            val last48h = now.minus(48, ChronoUnit.HOURS)

            val averages12 = logsRepository.fetchAveragesByPeriodOfTime(last12h.toEpochMilli())
            val averages24 = logsRepository.fetchAveragesByPeriodOfTime(last24h.toEpochMilli())
            val averages48 = logsRepository.fetchAveragesByPeriodOfTime(last48h.toEpochMilli())
            val events24 = logsRepository.fetchHeaterEventsByPeriodOfTime(last24h.toEpochMilli())
            combine(
                averages12,
                averages24,
                averages48,
                events24
            ) { avg12, avg24, avg48, events ->
                val average12 = Average(avg12.avgTempRead, avg12.avgHumidRead, 12)
                val average24 = Average(avg24.avgTempRead, avg24.avgHumidRead, 24)
                val average48 = Average(avg48.avgTempRead, avg48.avgHumidRead, 48)
                val eventHeaterOn = EventCount("Heater on", events.heaterOnCount, 24)
                UiState(
                    loading = false,
                    averages = UiState.AveragesUiState(
                        avg12h = average12,
                        avg24h = average24,
                        avg48h = average48
                    ),
                    events = UiState.EventsUiState(heaterOn = eventHeaterOn)
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState(loading = true)
        )

    fun onUiReady() {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            refreshTrigger.emit(Unit)
        }
    }

    data class UiState(
        var loading: Boolean = true,
        val averages: AveragesUiState? = null,
        val events: EventsUiState? = null
    ) {
        data class AveragesUiState(
            val avg12h: Average,
            val avg24h: Average,
            val avg48h: Average,
        )

        data class EventsUiState(
            val heaterOn: EventCount,
        )
    }
}
