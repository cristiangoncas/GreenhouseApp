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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
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
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cristiangoncas.greenhousemonitor.domain.entity.Averages
import com.cristiangoncas.greenhousemonitor.ui.screen.Screen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel, innerPadding: PaddingValues
) {

    val state by viewModel.state.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    val onRefresh = {
        println("Refreshing...")
        viewModel.refresh()
    }
    Screen {
        if (state.loading) {
            Loading()
        } else {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .pullToRefresh(
                        isRefreshing = state.loading,
                        state = pullToRefreshState,
                        onRefresh = onRefresh
                    )
            ) {
                Column {
                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        onClick = {
                            viewModel.refresh()
                        }
                    ) {
                        Text(text = "Refresh")
                    }
                    AveragesComponent(state)
                }
            }
        }

    }
    LaunchedEffect(Unit) {
        viewModel.onUiReady()
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
fun AveragesComponent(state: HomeViewModel.UiState) {
//    Box(
//        contentAlignment = Alignment.Center
//    ) {
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
//    }
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
