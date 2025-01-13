package com.cristiangoncas.greenhousemonitor.domain.models

data class EventCount(
    val event: String,
    val count: Int,
    val hours: Int
)
