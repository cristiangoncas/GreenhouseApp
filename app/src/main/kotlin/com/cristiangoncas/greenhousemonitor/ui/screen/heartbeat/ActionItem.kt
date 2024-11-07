package com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cristiangoncas.greenhousemonitor.R

@Composable
fun ActionItem(
    label: String,
    error: String = "",
    sendAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = label, fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
                IconButton(
                    modifier = Modifier
                        .weight(.2f),
                    onClick = {
                    sendAction()
                }) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "Done")
                }
            }
            if (error.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                )
            }
        }
    }
}

@Preview
@Composable
fun ActionItemPreview() {
    ActionItem(
        label = "Request health check",
        error = "Something went wrong with the request.",
        sendAction = {}
    )
}
