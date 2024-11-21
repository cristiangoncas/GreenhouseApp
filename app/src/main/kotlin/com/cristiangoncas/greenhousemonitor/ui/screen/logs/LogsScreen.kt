package com.cristiangoncas.greenhousemonitor.ui.screen.logs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cristiangoncas.greenhousemonitor.domain.entity.Event
import com.cristiangoncas.greenhousemonitor.domain.entity.LogEntry
import com.cristiangoncas.greenhousemonitor.ui.screen.Screen

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

@Preview
@Composable
fun LogsScreenPreview() {
    LogsScreen(
        viewModel = viewModel(),
        innerPadding = PaddingValues(0.dp)
    )
}

@Composable
fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(150.dp)
        )
    }
}

@Composable
fun LogItem(
    state: LogsViewModel.UiState,
    index: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            val log = state.logs[index]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = Event.getEventByValue(log.event).label
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            modifier = Modifier,
                            text = log.time
                        )
                        Text(
                            modifier = Modifier,
                            text = log.date
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(

                ) {
                    val event = Event.getEventByValue(log.event)
                    Text(text = event.label + ": ")
                    Text(text = log.data)

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