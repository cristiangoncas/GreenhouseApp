package com.cristiangoncas.greenhousemonitor.ui.screen.logs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cristiangoncas.data.remote.ConnectivityDataSource
import com.cristiangoncas.greenhousemonitor.domain.models.Event
import com.cristiangoncas.greenhousemonitor.domain.models.LogEntry
import com.cristiangoncas.greenhousemonitor.ui.common.Loading
import com.cristiangoncas.greenhousemonitor.ui.common.Screen
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun LogsScreen(
    innerPadding: PaddingValues,
    connectivityState: ConnectivityDataSource = koinInject(),
    viewModel: LogsViewModel = koinViewModel()
) {
    val isConnected by connectivityState.isConnected.collectAsState(initial = false)
    val onUiReady = {
        viewModel.onUiReady()
    }
    val onRefresh = {
        viewModel.refresh()
    }
    val state by viewModel.state.collectAsState()
    LogsScreen(
        innerPadding = innerPadding,
        isConnected = isConnected,
        state = state,
        onUiReady = onUiReady,
        onRefresh = onRefresh
    )
}

@Composable
fun LogsScreen(
    innerPadding: PaddingValues,
    isConnected: Boolean,
    state: LogsViewModel.UiState,
    onUiReady: () -> Unit,
    onRefresh: () -> Unit
) {
    if (!isConnected) {
        // TODO: Snack bar to inform there is no network
    }

    Screen {
        LogsContent(innerPadding, state, onRefresh)
        if (state.error != null) {
            // TODO: Snack bar
            println("Error: ${state.error}")
        }
        LaunchedEffect(Unit) {
            onUiReady()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsContent(innerPadding: PaddingValues, state: LogsViewModel.UiState, onRefresh: () -> Unit) {
    val pullRefreshState = rememberPullToRefreshState()

    if (state.loading) {
        Loading()
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullToRefresh(
                    isRefreshing = state.loading,
                    state = pullRefreshState,
                    onRefresh = onRefresh
                )
        ) {
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.logs.size) { index ->
                    LogItem(state, index)
                }
                if (state.logs.isEmpty()) {
                    item {
                        NoLogsToShow()
                    }
                }
            }
        }
    }
}

@Composable
fun NoLogsToShow() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(28.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(alignment = Alignment.Center),
                text = "No logs to show"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoLogsToShowPreview() {
    NoLogsToShow()
}

@Preview(showBackground = true)
@Composable
fun LogItemsPreview() {
    val uiState = LogsViewModel.UiState(
        loading = false,
        logs = listOf(
            LogEntry(
                id = 1,
                timestamp = 0,
                date = "12-12-2022",
                time = "12:12:12",
                data = "On",
                event = Event.HEATER.value
            ),
            LogEntry(
                id = 2,
                timestamp = 0,
                date = "12-12-2022",
                time = "12:12:12",
                data = "56.00",
                event = Event.HUMID_READ.value
            ),
            LogEntry(
                id = 3,
                timestamp = 0,
                date = "12-12-2022",
                time = "12:12:12",
                data = "18.00",
                event = Event.TEMP_READ.value
            )
        )
    )
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(uiState.logs.size) { index ->
            LogItem(uiState, index)
        }
    }
}
