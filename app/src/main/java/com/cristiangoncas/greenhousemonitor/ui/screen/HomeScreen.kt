package com.cristiangoncas.greenhousemonitor.ui.screen

import android.annotation.SuppressLint
import android.net.wifi.hotspot2.pps.HomeSp
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cristiangoncas.greenhousemonitor.business.entity.LogEntry
import com.cristiangoncas.greenhousemonitor.business.viewmodel.HomeViewModel
import java.lang.reflect.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.state.collectAsState()
    Screen {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Greenhouse Monitor") },
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
    println("Inflate HomeContent")
    LazyColumn(
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.logs.size) { index ->
            Column {
                val log = state.logs[index]
                Text(text = log.time)
                Text(text = log.id.toString())
                Text(text = log.event)
                Text(text = log.data)
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