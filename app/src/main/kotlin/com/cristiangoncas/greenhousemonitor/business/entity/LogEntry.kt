package com.cristiangoncas.greenhousemonitor.business.entity

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class LogEntry(
    var id: Int,
    var time: String,
    var data: String,
    var event: String,
) {

    @Transient
    var year: Int = 0
    @Transient
    var month: Int = 0
    @Transient
    var day: Int = 0
    @Transient
    var hour: String = "0"
    @Transient
    var minute: String = "0"
    @Transient
    var second: String = "0"

    init {
        val parts = time.split(" ")
        val dateParts = parts[0].split("-")
        val timeParts = parts[1].split(":")

        year = dateParts[0].toInt()
        month = dateParts[1].toInt()
        day = dateParts[2].toInt()
        hour = timeParts[0]
        minute = timeParts[1]
        second = timeParts[2]
    }
}