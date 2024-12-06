package com.cristiangoncas.greenhousemonitor.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cristiangoncas.greenhousemonitor.data.local.model.RawLogEntry

@Dao
interface RawLogDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertRawLogs(rawLogs: List<RawLogEntry>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertRawLog(rawLogs: RawLogEntry)

    @Query("SELECT * FROM RawLogEntry")
    fun fetchAllRawLogs(): List<RawLogEntry>

    @Query("SELECT * FROM RawLogEntry WHERE id > :id")
    fun fetchAllRawLogsFromAfterId(id: Int): List<RawLogEntry>
}