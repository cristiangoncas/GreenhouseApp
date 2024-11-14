package com.cristiangoncas.greenhousemonitor.domain.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cristiangoncas.greenhousemonitor.domain.entity.RawLogEntry

@Dao
interface RawLogDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertRawLog(rawLog: RawLogEntry)

    @Query("SELECT * FROM RawLogEntry")
    fun fetchAllRawLogs(): List<RawLogEntry>

    @Query("SELECT * FROM RawLogEntry WHERE id > :id")
    fun fetchAllRawLogsFromAfterId(id: Int): List<RawLogEntry>
}