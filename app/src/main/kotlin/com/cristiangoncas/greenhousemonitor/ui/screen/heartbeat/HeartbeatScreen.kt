package com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cristiangoncas.greenhousemonitor.domain.entity.HeartBeat
import com.cristiangoncas.greenhousemonitor.ui.screen.Screen

@Composable
fun HeartBeatScreen(viewModel: HeartbeatViewModel, innerPadding: PaddingValues) {
    val state by viewModel.state.collectAsState()

    Screen {
        if (state.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = "Greenhouse Monitor",
                        textAlign = TextAlign.Center
                    )
                }
                item {
                    HeartbeatContent(state, viewModel)
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.onUiReady()
    }
}

@Composable
fun HeartbeatContent(
    state: HeartbeatViewModel.UiState, viewModel: HeartbeatViewModel
) {
    ActionItem(
        label = "Request health check",
        error = state.errors["setHealthCheck"] ?: "",
        sendAction = {
            viewModel.requestHealthCheck()
            // Disable button and update UI accordingly to avoid multiple clicks
        },
    )
    ActionItem(
        label = "Reset defaults",
        error = state.errors["resetDefaults"] ?: "",
        sendAction = { viewModel.resetDefaults() },
    )

    ValueItem(label = "Max Temp",
        value = state.heartBeat.maxTemp ?: "",
        error = state.errors["maxTemp"] ?: "",
        validateAndSend = { viewModel.setMaxTemp(it) })

    ValueItem(
        label = "Min Temp",
        value = state.heartBeat.minTemp ?: "",
        error = state.errors["minTemp"] ?: "",
        validateAndSend = { viewModel.setMinTemp(it) })

    ValueItem(label = "Morning Time",
        value = state.heartBeat.morningTime ?: "",
        error = state.errors["morningTime"] ?: "",
        validateAndSend = { viewModel.setMorningTime(it) })

    ValueItem(label = "Night Time",
        value = state.heartBeat.nightTime ?: "",
        error = state.errors["nightTime"] ?: "",
        validateAndSend = { viewModel.setNightTime(it) })

    ValueItem(label = "Night Temp Diff",
        value = state.heartBeat.nightTempDifference ?: "",
        error = state.errors["nightTempDifference"] ?: "",
        validateAndSend = { viewModel.setNightTempDifference(it) })

    ValueItem(label = "Heartbeat Period",
        value = state.heartBeat.heartbeatPeriod ?: "",
        error = state.errors["heartbeatPeriod"] ?: "",
        validateAndSend = { viewModel.setHeartbeatPeriod(it) })
}

@Preview(showBackground = true)
@Composable
fun HeartbeatContentPreview() {
    val uiState = HeartbeatViewModel.UiState(
        loading = false, heartBeat = HeartBeat(
            maxTemp = "22", minTemp = "17"
        )
    )
    HeartbeatContent(
        state = uiState,
        viewModel = viewModel()
    )
}
