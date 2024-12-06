package com.cristiangoncas.greenhousemonitor.domain.entity

data class EventCount(
    val event: String,
    val count: Int,
    val hours: Int
)
