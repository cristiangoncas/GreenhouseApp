package com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.cristiangoncas.greenhousemonitor.business.entity.HeartBeat
import com.cristiangoncas.greenhousemonitor.ui.common.ArrowBackIcon
import com.cristiangoncas.greenhousemonitor.ui.screen.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeartBeatScreen(viewModel: HeartbeatViewModel, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Screen {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = { ArrowBackIcon(onBack = onBack) },
                    title = { Text(text = "Heartbeat") },
                    scrollBehavior = scrollBehavior
                )
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            contentWindowInsets = WindowInsets.safeDrawing
        )
        { innerPadding ->
            viewModel.uiReady()
            if (state.loading) {
                CircularProgressIndicator()
            } else {
                HeartbeatContent(innerPadding, state, viewModel)
            }
        }
    }
}

@Composable
fun HeartbeatContent(
    innerPadding: PaddingValues, state: HeartbeatViewModel.UiState, viewModel: HeartbeatViewModel
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(
                state = scrollState
            ),
    ) {
        ActionItem(
            label = "Request health check",
            error = state.errors["setHealthCheck"] ?: "",
            sendAction = { viewModel.requestHealthCheck() },
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
        innerPadding = PaddingValues(),
        state = uiState,
        viewModel = HeartbeatViewModel()
    )
}
