package com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat

import android.widget.EditText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            }
        ) { innerPadding ->
            viewModel.uiReady()
            if (state.loading) {
                CircularProgressIndicator()
            } else {
                HeartbeatContent(innerPadding, state)
            }
        }
    }
}

@Composable
fun HeartbeatContent(innerPadding: PaddingValues, state: HeartbeatViewModel.UiState) {
    if (state.heartBeat.values.isEmpty()) {
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "No data available"
            )
        }
    } else {
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.heartBeat.values.size) { index ->
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    val key = state.heartBeat.values.keys.elementAt(index)
                    val value = state.heartBeat.values.values.elementAt(index)
                    Text(
                        text = key,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                    Text(
                        text = value,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                    Checkbox(
                        modifier = Modifier
                            .width(25.dp),
                        checked = false,
                        onCheckedChange = {}
                    )
                    TextField(value = value, onValueChange = {
                        // TODO: Call view model to update the value
                    })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeartbeatContentPreview() {
    val uiState = HeartbeatViewModel.UiState(
        loading = false,
        heartBeat = HeartBeat(
            mapOf(
                "maxTemp" to "22",
                "minTemp" to "17"
            )
        )
    )
    HeartbeatContent(PaddingValues(), uiState)
}

@Preview(showBackground = true)
@Composable
fun HeartbeatContentPreviewEmpty() {
    val uiState = HeartbeatViewModel.UiState(
        loading = false,
        heartBeat = HeartBeat(
            emptyMap()
        )
    )
    HeartbeatContent(PaddingValues(), uiState)
}