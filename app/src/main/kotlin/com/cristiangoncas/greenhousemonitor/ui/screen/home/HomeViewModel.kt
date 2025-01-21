package com.cristiangoncas.greenhousemonitor.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristiangoncas.greenhousemonitor.domain.models.Average
import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.greenhousemonitor.domain.models.EventCount
import com.cristiangoncas.usecases.Average12hUseCase
import com.cristiangoncas.usecases.Average24hUseCase
import com.cristiangoncas.usecases.Average48hUseCase
import com.cristiangoncas.usecases.HeaterEvents24hUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    average12hUseCase: Average12hUseCase,
    average24hUseCase: Average24hUseCase,
    average48hUseCase: Average48hUseCase,
    heaterEvents24hUseCase: HeaterEvents24hUseCase,
) : ViewModel() {

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<UiState> = refreshTrigger
        .onStart { emit(Unit) }
        .flatMapLatest {
            val averages12 = average12hUseCase()
            val averages24 = average24hUseCase()
            val averages48 = average48hUseCase()
            val events = heaterEvents24hUseCase()
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
