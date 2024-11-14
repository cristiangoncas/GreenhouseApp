package com.cristiangoncas.greenhousemonitor.ui.screen.home

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cristiangoncas.greenhousemonitor.domain.entity.Event
import com.cristiangoncas.greenhousemonitor.domain.entity.LogEntry
import com.cristiangoncas.greenhousemonitor.domain.entity.RemoteLogEntry
import com.cristiangoncas.greenhousemonitor.ui.screen.Screen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(viewModel: HomeViewModel, onHeartBeatClicked: () -> Unit) {

    Screen {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Greenhouse Monitor") },
                    actions = {
                        IconButton(onClick = onHeartBeatClicked) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "HeartBeat"
                            )

                        }
                    }
                )
            },
        ) { innerPadding ->
            HomeContent(
                innerPadding = innerPadding,
                viewModel = viewModel
            )
            viewModel.uiReady()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    innerPadding: PaddingValues,
    viewModel: HomeViewModel
) {
    val state by viewModel.state.collectAsState()
    if (state.loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(50.dp)
            )
        }
    } else {
        val pullRefreshState = rememberPullToRefreshState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pullToRefresh(
                    isRefreshing = false,
                    state = pullRefreshState,
                    onRefresh = { viewModel.refresh() }
                )
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.logs.size) { index ->
                    LogItem(state, index)
                }
            }
        }
    }
}

@Composable
fun LogItem(
    state: HomeViewModel.UiState,
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
                        contentDescription = log.event.label
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
                    Text(text = log.event.label + ": ")
                    Text(text = log.data)

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogItemsPreview() {
    val uiState = HomeViewModel.UiState(
        loading = false,
        logs = listOf(
            LogEntry(
                id = 1,
                timestamp = 0,
                date = "12-12-2022",
                time = "12:12:12",
                data = "On",
                event = Event.HEATER
            ),
            LogEntry(
                id = 2,
                timestamp = 0,
                date = "12-12-2022",
                time = "12:12:12",
                data = "56.00",
                event = Event.HUMID_READ
            ),
            LogEntry(
                id = 3,
                timestamp = 0,
                date = "12-12-2022",
                time = "12:12:12",
                data = "18.00",
                event = Event.TEMP_READ
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

@Preview(showBackground = true)
@Composable
fun HomeContentLoadingPreview() {
    val uiState = HomeViewModel.UiState(
        loading = true,
        logs = listOf()
    )
    HomeScreen(viewModel = viewModel(), onHeartBeatClicked = {})
}
