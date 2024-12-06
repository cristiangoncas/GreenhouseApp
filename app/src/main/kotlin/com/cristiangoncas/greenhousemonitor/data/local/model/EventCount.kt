package com.cristiangoncas.greenhousemonitor.data.local.model

data class EventCount(
    val event: String,
    val count: Int,
    val hours: Int
)
