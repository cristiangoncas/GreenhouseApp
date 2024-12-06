package com.cristiangoncas.greenhousemonitor.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristiangoncas.greenhousemonitor.domain.data.repository.GreenhouseRepository
import com.cristiangoncas.greenhousemonitor.domain.entity.Average
import com.cristiangoncas.greenhousemonitor.domain.entity.EventCount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit

class HomeViewModel(private val repository: GreenhouseRepository) : ViewModel() {

    private var _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    fun onUiReady() {
        refresh()
    }

    fun refresh() {
        _state.update { currentState ->
            currentState.copy(loading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val last12h = Instant.now().minus(12, ChronoUnit.HOURS)
            val last24h = Instant.now().minus(24, ChronoUnit.HOURS)
            val last48h = Instant.now().minus(48, ChronoUnit.HOURS)

            val averages12 = repository.fetchAveragesByPeriodOfTime(last12h.toEpochMilli())
//                .collect { averageTempHumid ->
//                    _state.update { currentState ->
//                        val averages = mutableListOf<Average>()
//                        averages.addAll(currentState.averages)
//                        averages.add(
//                            Average(
//                                averageTempHumid.avgTempRead,
//                                averageTempHumid.avgHumidRead,
//                                12
//                            )
//                        )
//                        averages.sortBy { it.hours }
//
//                        currentState.copy(averages = averages, loading = false)
//                    }
//
//                }
            val averages24 = repository.fetchAveragesByPeriodOfTime(last24h.toEpochMilli())
//                .collect { averageTempHumid ->
//                    _state.update { currentState ->
//                        val averages = mutableListOf<Average>()
//                        averages.addAll(currentState.averages)
//                        averages.add(
//                            Average(
//                                averageTempHumid.avgTempRead,
//                                averageTempHumid.avgHumidRead,
//                                24
//                            )
//                        )
//                        averages.sortBy { it.hours }
//
//                        currentState.copy(averages = averages, loading = false)
//                    }
//
//                }
            val averages48 = repository.fetchAveragesByPeriodOfTime(last48h.toEpochMilli())
//                .collect { averageTempHumid ->
//                    _state.update { currentState ->
//                        val averages = mutableListOf<Average>()
//                        averages.addAll(currentState.averages)
//                        averages.add(
//                            Average(
//                                averageTempHumid.avgTempRead,
//                                averageTempHumid.avgHumidRead,
//                                48
//                            )
//                        )
//                        averages.sortBy { it.hours }
//
//                        currentState.copy(averages = averages, loading = false)
//                    }
//
//                }
            val events24 = repository.fetchHeaterEventsByPeriodOfTime(last24h.toEpochMilli())
//                .collect { heaterEvents ->
//                    _state.update { currentState ->

//                    }
//
//                }

            val combinedFlows = combine(
                averages12,
                averages24,
                averages48,
                events24
            ) { avg12, avg24, avg48, events ->
                _state.update { currentState ->
                    val average12 = Average(avg12.avgTempRead, avg12.avgHumidRead, 12)
                    val average24 = Average(avg24.avgTempRead, avg24.avgHumidRead, 24)
                    val average48 = Average(avg48.avgTempRead, avg48.avgHumidRead, 48)

                    val eventHeaterOn = EventCount("Heater on", events.heaterOnCount, 24)
                    currentState.copy(
                        loading = false,
                        averages = UiState.AveragesUiState(
                            avg12h = average12,
                            avg24h = average24,
                            avg48h = average48
                        ),
                        events = UiState.EventsUiState(heaterOn = eventHeaterOn),
                    )
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState(loading = true)
            ).collect()
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
