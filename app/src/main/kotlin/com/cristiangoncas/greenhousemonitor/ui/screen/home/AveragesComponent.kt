package com.cristiangoncas.greenhousemonitor.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cristiangoncas.greenhousemonitor.data.local.model.Average

@Composable
fun AveragesComponent(average: Average) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Optional padding for the entire row
        horizontalArrangement = Arrangement.SpaceAround // Space evenly between the components
    ) {
        AverageDisplayComponent(
            label = "Avg Temperature ${average.hours}h",
            value = average.avgTemp.toString()
        )

        AverageDisplayComponent(
            label = "Avg Humidity ${average.hours}h",
            value = average.avgHumid.toString()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAverages() {
    Column {
        AveragesComponent(
            Average(
                avgTemp = 22f,
                avgHumid = 78f,
                hours = 6
            )
        )
        AveragesComponent(
            Average(
                avgTemp = 22f,
                avgHumid = 78f,
                hours = 12
            )
        )
    }
}
