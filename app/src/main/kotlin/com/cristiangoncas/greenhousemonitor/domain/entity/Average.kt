package com.cristiangoncas.greenhousemonitor.domain.entity

data class Average(
    val avgTemp: Float = 0f,
    val avgHumid: Float = 0f,
    val hours: Int = 0
)
