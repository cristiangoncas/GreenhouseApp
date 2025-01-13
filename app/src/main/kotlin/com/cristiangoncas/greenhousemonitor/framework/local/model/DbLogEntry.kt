package com.cristiangoncas.greenhousemonitor.framework.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbLogEntry(
    @PrimaryKey
    var id: Int,
    var timestamp: Long,
    var date: String,
    var time: String,
    var data: String,
    var event: String
)
