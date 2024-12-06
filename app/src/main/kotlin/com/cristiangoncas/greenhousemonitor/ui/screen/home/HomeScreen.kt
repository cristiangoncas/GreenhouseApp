package com.cristiangoncas.greenhousemonitor.ui.screen.home

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cristiangoncas.greenhousemonitor.domain.entity.Average
import com.cristiangoncas.greenhousemonitor.domain.entity.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.ui.screen.Screen

@Composable
fun HomeScreen(
    viewModel: HomeViewModel, innerPadding: PaddingValues
) {
    val state by viewModel.state.collectAsState()
    val onRefresh = {
        viewModel.refresh()
    }
    Screen {
        HomeContent(innerPadding, state, onRefresh)
    }
    LaunchedEffect(Unit) {
        viewModel.onUiReady()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    innerPadding: PaddingValues,
    state: HomeViewModel.UiState,
    onRefresh: () -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    if (state.loading) {
        Loading()
    } else {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .padding(top = 56.dp)
                .fillMaxSize()
                .pullToRefresh(
                    isRefreshing = state.loading,
                    state = pullToRefreshState,
                    onRefresh = onRefresh
                )
        ) {
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                state.averages?.let { averages ->
                    AveragesComponent(averages.avg12h)
                    AveragesComponent(averages.avg24h)
                    AveragesComponent(averages.avg48h)
                }

                state.events?.let { events ->
                    EventCountComponent(events.heaterOn.event, events.heaterOn.count, events.heaterOn.hours)
                }
            }
        }
    }
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

@Preview(showBackground = true)
@Composable
fun PreviewLoading() {
    Loading()
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
fun AveragesComponent(average: Average) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Optional padding for the entire row
        horizontalArrangement = Arrangement.SpaceAround // Space evenly between the components
    ) {
        AverageDisplayComponent(
            label = "Avg Temperature ${average.hours}h",
            value = average.avgTemp.toString()
        )

        AverageDisplayComponent(
            label = "Avg Humidity ${average.hours}h",
            value = average.avgHumid.toString()
        )
    }
//    }
}

@Composable
fun EventCountComponent(event: String, count: Int, hours: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$event events - ${hours}h",
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = count.toString(),
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventCountComponentPreview() {
    EventCountComponent(event = "Heater on", count = 2, hours = 24)
}

@Preview(showBackground = true)
@Composable
fun PreviewAverages() {
    Column {
        AveragesComponent(
            Average(
                avgTemp = 22f,
                avgHumid = 78f,
                hours = 6
            )
        )
        AveragesComponent(
            Average(
                avgTemp = 22f,
                avgHumid = 78f,
                hours = 12
            )
        )
    }
}
