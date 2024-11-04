package com.cristiangoncas.greenhousemonitor.ui.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cristiangoncas.greenhousemonitor.business.entity.LogEntry
import com.cristiangoncas.greenhousemonitor.ui.screen.Screen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(viewModel: HomeViewModel, onHeartBeatClicked: () -> Unit) {
    val state by viewModel.state.collectAsState()
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
            viewModel.uiReady()
            if (state.loading) {
                CircularProgressIndicator()
            } else {
                HomeContent(innerPadding, state)
            }
        }
    }
}

@Composable
fun HomeContent(innerPadding: PaddingValues, state: HomeViewModel.UiState) {
    LazyColumn(
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.logs.size) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    val log = state.logs[index]
                    Text(text = "${log.hour}:${log.minute}")
                    Text(text = "Event: ${log.event}")
                    Text(text = "Data: ${log.data}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    val uiState = HomeViewModel.UiState(
        loading = false,
        logs = listOf(
            LogEntry(
                id = 1,
                time = "2024-10-15 23:07:50",
                event = "isNight",
                data = "21:57:50 - Yes"
            ),
            LogEntry(
                id = 2,
                time = "2024-10-15 22:07:50",
                event = "tempRead",
                data = "17.00"
            ),
            LogEntry(
                id = 3,
                time = "2024-10-15 21:07:50",
                event = "humRead",
                data = "53.00"
            )
        )
    )
    HomeContent(PaddingValues(0.dp), uiState)
}