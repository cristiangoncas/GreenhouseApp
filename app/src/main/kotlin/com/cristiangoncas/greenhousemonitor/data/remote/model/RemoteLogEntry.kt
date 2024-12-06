package com.cristiangoncas.greenhousemonitor.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteLogEntry(
    var id: Int,
    var time: String,
    var data: String,
    var event: String
)
