package com.cristiangoncas.greenhousemonitor.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristiangoncas.greenhousemonitor.data.local.model.Average
import com.cristiangoncas.greenhousemonitor.data.local.model.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.data.local.model.CustomResult
import com.cristiangoncas.greenhousemonitor.data.local.model.Event
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
            val events = logsRepository.fetchHeaterEventsByPeriodOfTime(last24h.toEpochMilli())
            combine(
                averages12,
                averages24,
                averages48,
                events
            ) { avg12, avg24, avg48, events24 ->

                val uiState = UiState(loading = true)

                val errors = mutableListOf<String>()
                var averages12Data: Average? = null
                var averages24Data: Average? = null
                var averages48Data: Average? = null
                var events24Data: EventCount? = null
                when (avg12) {
                    is CustomResult.Success -> {
                        averages12Data =
                            Average(avg12.data.avgTempRead, avg12.data.avgHumidRead, 12)
                    }

                    is CustomResult.Error -> {
                        errors.add(avg12.message)
                    }

                    is CustomResult.Loading -> {
                        uiState.loading = true
                    }
                }

                when (avg24) {
                    is CustomResult.Success -> {
                        averages24Data =
                            Average(avg24.data.avgTempRead, avg24.data.avgHumidRead, 24)
                    }

                    is CustomResult.Error -> {
                        errors.add(avg24.message)
                    }

                    is CustomResult.Loading -> {
                        uiState.loading = true
                    }
                }
                when (avg48) {
                    is CustomResult.Success -> {
                        averages48Data =
                            Average(avg48.data.avgTempRead, avg48.data.avgHumidRead, 48)
                    }

                    is CustomResult.Error -> {
                        errors.add(avg48.message)
                    }

                    is CustomResult.Loading -> {
                        uiState.loading = true
                    }
                }
                when (events24) {
                    is CustomResult.Success -> {
                        events24Data = EventCount("Heater on", events24.data.heaterOnCount, 24)
                    }

                    is CustomResult.Error -> {
                        errors.add(events24.message)
                    }

                    is CustomResult.Loading -> {
                        uiState.loading = true
                    }
                }
                if (averages12Data != null && averages24Data != null && averages48Data != null && events24Data != null) {
                    UiState(
                        loading = false,
                        averages = UiState.AveragesUiState(
                            avg12h = averages12Data,
                            avg24h = averages24Data,
                            avg48h = averages48Data
                        ),
                        events = UiState.EventsUiState(heaterOn = events24Data)
                    )
                } else {
                    UiState(loading = false, errors = listOf("Error fetching data"))
                }
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
        val events: EventsUiState? = null,
        val errors: List<String> = emptyList()
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
