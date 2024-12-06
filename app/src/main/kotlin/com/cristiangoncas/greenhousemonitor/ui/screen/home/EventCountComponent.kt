package com.cristiangoncas.greenhousemonitor.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
