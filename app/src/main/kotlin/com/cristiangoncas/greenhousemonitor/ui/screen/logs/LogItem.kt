package com.cristiangoncas.greenhousemonitor.ui.screen.logs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cristiangoncas.greenhousemonitor.domain.entity.Event

@Composable
fun LogItem(
    state: LogsViewModel.UiState,
    index: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            val log = state.logs[index]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = Event.getEventByValue(log.event).label
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            modifier = Modifier,
                            text = log.time
                        )
                        Text(
                            modifier = Modifier,
                            text = log.date
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(

                ) {
                    val event = Event.getEventByValue(log.event)
                    Text(text = event.label + ": ")
                    Text(text = log.data)

                }
            }
        }
    }
}