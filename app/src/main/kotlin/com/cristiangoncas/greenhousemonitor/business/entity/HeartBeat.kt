package com.cristiangoncas.greenhousemonitor.business.entity

import kotlinx.serialization.Serializable

@Serializable
data class HeartBeat(
    var values: Map<String, String>
)
