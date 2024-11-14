package com.cristiangoncas.greenhousemonitor.domain.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cristiangoncas.greenhousemonitor.domain.entity.LogEntry
import java.util.Calendar

@Dao
interface LogEntryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLogEntry(logEntry: LogEntry)

    @Query("SELECT * FROM LogEntry ORDER BY id DESC")
    fun fetchAllLogs(): List<LogEntry>

    @Query("SELECT * FROM LogEntry WHERE timestamp >= :past24hours ORDER BY id DESC")
    fun fetchLogEntriesLast24hFromPointInTime(past24hours: Long): List<LogEntry>
}
