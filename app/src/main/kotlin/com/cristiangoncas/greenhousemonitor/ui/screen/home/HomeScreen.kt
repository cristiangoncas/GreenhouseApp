package com.cristiangoncas.greenhousemonitor.ui.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cristiangoncas.greenhousemonitor.domain.entity.Averages
import com.cristiangoncas.greenhousemonitor.domain.entity.Event
import com.cristiangoncas.greenhousemonitor.domain.entity.LogEntry
import com.cristiangoncas.greenhousemonitor.ui.screen.Screen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onHeartBeatClicked: () -> Unit,
    onLogsClicked: () -> Unit
) {

    Screen {
        Scaffold(
            topBar = {
                HomeTopBar(onHeartBeatClicked, onLogsClicked)
            },
        ) { innerPadding ->
            HomeContent(
                innerPadding = innerPadding,
                viewModel = viewModel
            )
            viewModel.onUiReady()
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeTopBar(onHeartBeatClicked: () -> Unit, onLogsClicked: () -> Unit) {
    TopAppBar(
        title = { Text(text = "Greenhouse Monitor") },
        actions = {
            IconButton(onClick = onHeartBeatClicked) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "HeartBeat"
                )
            }
            IconButton(onClick = onLogsClicked) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Logs"
                )
            }
        }
    )
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
            AveragesComponent(state)
        }
    }
}

@Composable
fun AverageDisplayComponent(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .height(100.dp)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun AveragesComponent(state: HomeViewModel.UiState) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), // Optional padding for the entire row
            horizontalArrangement = Arrangement.SpaceAround // Space evenly between the components
        ) {
            AverageDisplayComponent(
                label = "Avg Temperature 6h",
                value = state.averages.avgTempRead.toString()
            )

            AverageDisplayComponent(
                label = "Avg Humidity 6h",
                value = state.averages.avgHumidRead.toString()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAverages() {
    val state = HomeViewModel.UiState(
        loading = false,
        averages = Averages(
            avgTempRead = 22f,
            avgHumidRead = 78f
        )
    )
    AveragesComponent(state)
}
