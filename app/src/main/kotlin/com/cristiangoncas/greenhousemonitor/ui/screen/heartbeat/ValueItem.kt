package com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ValueItem(
    label: String,
    value: String,
    error: String = "",
    validateAndSend: (newValue: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var newValue by remember { mutableStateOf(value) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
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
                    modifier = Modifier
                        .weight(1f),
                    text = label,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
                Text(
                    modifier = Modifier
                        .weight(.2f),
                    text = "($value)"
                )
                OutlinedTextField(modifier = Modifier
                    .width(65.dp)
                    .height(55.dp),
                    value = newValue,
                    onValueChange = {
                        newValue = it
                    }
                )
                IconButton(
                    modifier = Modifier
                        .weight(.2f),
                    enabled = newValue.isNotEmpty(), onClick = {
                        validateAndSend(newValue)
                    }) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "Done")
                }
            }
            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ValueItemPreview() {
    ValueItem(
        label = "Night temp diff",
        value = "22",
        error = "Temperature out of range",
        validateAndSend = {})
}
