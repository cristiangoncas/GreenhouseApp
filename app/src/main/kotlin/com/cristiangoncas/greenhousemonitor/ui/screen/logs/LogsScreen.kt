package com.cristiangoncas.greenhousemonitor.ui.screen.logs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cristiangoncas.greenhousemonitor.data.local.model.Event
import com.cristiangoncas.greenhousemonitor.data.local.model.LogEntry
import com.cristiangoncas.greenhousemonitor.ui.common.Screen
import com.cristiangoncas.greenhousemonitor.ui.common.Loading

@Composable
fun LogsScreen(
    viewModel: LogsViewModel, innerPadding: PaddingValues
) {
    val state by viewModel.state.collectAsState()
    val onRefresh = { viewModel.refresh() }
    Screen {
        LogsContent(innerPadding, state, onRefresh)
    }
    LaunchedEffect(Unit) {
        viewModel.onUiReady()
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
            }
        }
    }
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
