package com.cristiangoncas.greenhousemonitor.ui.screen.logs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristiangoncas.greenhousemonitor.data.local.model.LogEntry
import com.cristiangoncas.greenhousemonitor.data.repository.LogsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LogsViewModel(private val logsRepository: LogsRepository) : ViewModel() {

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<UiState> = refreshTrigger
        .onStart { emit(Unit) }
        .flatMapLatest {
            logsRepository.last24hLogs
                .map { UiState(loading = false, logs = it) }
                .onStart { emit(UiState(loading = true)) }
                .catch {
                    //TODO: Error handling
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
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
        val logs: List<LogEntry> = emptyList(),
    )
}
