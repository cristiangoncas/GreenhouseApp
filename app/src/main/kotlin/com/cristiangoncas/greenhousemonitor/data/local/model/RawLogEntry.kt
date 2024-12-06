package com.cristiangoncas.greenhousemonitor.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cristiangoncas.greenhousemonitor.data.remote.model.RemoteLogEntry
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Entity
data class RawLogEntry(
    @PrimaryKey
    var id: Int,
    var time: Long,
    var data: String,
    var event: String,
) {

    companion object {
        fun fromRemoteLogEntry(remoteLogEntry: RemoteLogEntry): RawLogEntry {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply {
                timeZone = TimeZone.getDefault()
            }
            val date = dateFormat.parse(remoteLogEntry.time)
                ?: throw IllegalArgumentException("Invalid date format")
            return RawLogEntry(
                id = remoteLogEntry.id,
                time = date.time,
                data = remoteLogEntry.data,
                event = remoteLogEntry.event
            )
        }
    }
}
