package com.cristiangoncas.greenhousemonitor.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LogEntry(
    @PrimaryKey
    var id: Int,
    var timestamp: Long,
    var date: String,
    var time: String,
    var data: String,
    var event: String
)

enum class Event(val value: String, val label: String) {
    HUMID_READ("humidRead", "Humidity"),
    TEMP_READ("tempRead", "Temperature"),
    IS_NIGHT("isNight", "Is night?"),
    HEATER("heater", "Heater"),
    MAX_TEMP("maxTemp", "Max temperature"),
    MIN_TEMP("minTemp", "Min temperature"),
    MORNING_TIME("morningTime", "Morning time"),
    NIGHT_TIME("nightTime", "Night time"),
    NIGHT_TEMP_DIFFERENCE("nightTempDifference", "Night temperature difference"),
    HEARTBEAT("heartBeat", "Heart request"),
    UNKNOWN("unknown", "Unknown");

    companion object {
    fun getEventByValue(value: String) =
        when (value) {
            "humidRead" -> HUMID_READ
            "tempRead" -> TEMP_READ
            "isNight" -> IS_NIGHT
            "heater" -> HEATER
            "maxTemp" -> MAX_TEMP
            "minTemp" -> MIN_TEMP
            "morningTime" -> MORNING_TIME
            "nightTime" -> NIGHT_TIME
            "nightTempDifference" -> NIGHT_TEMP_DIFFERENCE
            "heartBeat" -> HEARTBEAT
            else -> UNKNOWN
        }
        }
}
