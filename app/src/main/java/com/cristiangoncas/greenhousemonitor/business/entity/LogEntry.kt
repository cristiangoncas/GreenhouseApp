package com.cristiangoncas.greenhousemonitor.business.entity

import kotlinx.serialization.Serializable

@Serializable
data class LogEntry(
    var id: Int,
    var time: String,
    var data: String,
    var event: String,
)