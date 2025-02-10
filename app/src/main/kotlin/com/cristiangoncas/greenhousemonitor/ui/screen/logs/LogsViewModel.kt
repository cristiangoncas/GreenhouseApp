package com.cristiangoncas.greenhousemonitor.ui.screen.logs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.greenhousemonitor.domain.models.LogEntry
import com.cristiangoncas.usecases.FetchLastLogsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LogsViewModel(
    private val fetchLastLogsUseCase: FetchLastLogsUseCase,
    private val refreshDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<UiState> = refreshTrigger
        .onStart {
            emit(Unit)
        }
        .flatMapLatest {
            fetchLastLogsUseCase()
                .map {
                    when (it) {
                        is CustomResult.Success -> {
                            UiState(loading = false, logs = it.data)
                        }

                        is CustomResult.Error -> {
                            UiState(loading = false, error = it.message)
                        }

                        is CustomResult.Loading -> UiState(loading = true)
                    }
                }
                .catch {
                    UiState(loading = false, error = it.message)
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
        viewModelScope.launch(refreshDispatcher) {
            refreshTrigger.emit(Unit)
        }
    }

    data class UiState(
        var loading: Boolean = true,
        val logs: List<LogEntry> = emptyList(),
        val error: String? = null
    )
}
