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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cristiangoncas.greenhousemonitor.R

@Composable
fun ValueItem(
    label: String,
    value: String,
    error: String = "",
    validateAndSend: (newValue: String) -> Unit
) {
    var newValue by remember { mutableStateOf(value) }
    Card(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = label, fontSize = MaterialTheme.typography.titleLarge.fontSize)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.current_value))
                    Text(text = value)
                }
                TextField(modifier = Modifier.width(60.dp), value = newValue, onValueChange = {
                    newValue = it
                })
                IconButton(enabled = newValue.isNotEmpty(), onClick = {
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
        label = "Max Temp",
        value = "22",
        error = "Temperature out of range",
        validateAndSend = {})
}