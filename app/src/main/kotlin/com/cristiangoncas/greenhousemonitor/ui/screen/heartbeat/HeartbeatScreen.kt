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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cristiangoncas.greenhousemonitor.R
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
        ValueItem(label = "Max Temp",
            value = state.heartBeat.maxTemp ?: "",
            error = state.errors["maxTemp"] ?: "",
            validateAndSend = { viewModel.setMaxTemp(it) })

        ValueItem(
            label = "Min Temp",
            value = state.heartBeat.minTemp ?: "",
            validateAndSend = { viewModel.setMinTemp(it) })

        ValueItem(label = "Morning Time",
            value = state.heartBeat.morningTime ?: "",
            validateAndSend = { viewModel.setMorningTime(it) })

        ValueItem(label = "Night Time",
            value = state.heartBeat.nightTime ?: "",
            validateAndSend = { viewModel.setNightTime(it) })

        ValueItem(label = "Night Temp Difference",
            value = state.heartBeat.nightTempDifference ?: "",
            validateAndSend = { viewModel.setNightTempDifference(it) })

        ValueItem(label = "Reset Defaults",
            value = state.heartBeat.resetDefaults ?: "",
            validateAndSend = { viewModel.setResetDefaults() })

        ValueItem(label = "Heartbeat Period",
            value = state.heartBeat.heartbeatPeriod ?: "",
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
