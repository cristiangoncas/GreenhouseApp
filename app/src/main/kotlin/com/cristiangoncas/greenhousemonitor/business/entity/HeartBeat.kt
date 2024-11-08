package com.cristiangoncas.greenhousemonitor.business.entity

import kotlinx.serialization.Serializable

@Serializable
data class HeartBeat(
    val maxTemp: String? = null,
    val minTemp: String? = null,
    val morningTime: String? = null,
    val nightTime: String? = null,
    val nightTempDifference: String? = null,
    val resetDefaults: String? = null,
    val heartbeatPeriod: String? = null
) {
    fun allEmpty(): Boolean {
        return maxTemp.isNullOrEmpty() &&
                minTemp.isNullOrEmpty() &&
                morningTime == null &&
                nightTime == null &&
                nightTempDifference == null &&
                resetDefaults == null &&
                heartbeatPeriod == null
    }
}
