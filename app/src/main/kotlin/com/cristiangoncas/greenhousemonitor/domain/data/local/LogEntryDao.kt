package com.cristiangoncas.greenhousemonitor.domain.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cristiangoncas.greenhousemonitor.domain.entity.Averages
import com.cristiangoncas.greenhousemonitor.domain.entity.LogEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface LogEntryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLogEntries(logEntries: List<LogEntry>)

    @Query("SELECT id FROM LogEntry ORDER BY id DESC LIMIT 1")
    fun fetchLastLogEntryId(): Long

    @Query("SELECT * FROM LogEntry ORDER BY id DESC")
    fun fetchAllLogs(): Flow<List<LogEntry>>

    @Query("SELECT * FROM LogEntry WHERE timestamp >= :past24hours ORDER BY id DESC")
    fun fetchLogEntriesLast24hFromPointInTime(past24hours: Long): Flow<List<LogEntry>>

    // Method to fetch logs from the last 6h, for a given event (tempRead for example), summing up the values and returning the average.
    @Query("SELECT \n" +
            "    ROUND(AVG(CASE WHEN event = 'tempRead' THEN CAST(REPLACE(data, '.', '') AS REAL) / 100.0 END), 2) AS avgTempRead,\n" +
            "    ROUND(AVG(CASE WHEN event = 'humidRead' THEN CAST(REPLACE(data, '.', '') AS REAL) / 100.0 END), 2) AS avgHumidRead\n" +
            "FROM LogEntry \n" +
            "WHERE (event = 'tempRead' OR event = 'humidRead') \n" +
            "AND timestamp >= :period")
    fun fetchAverageTempByPeriodOfTime(period: Long): Flow<Averages>

    // Method to count the amount of logs. Will be used to do a fetch all logs if empty.
    @Query("SELECT COUNT(*) == 0 FROM LogEntry")
    fun areLogsEmpty(): Int

    // Method to check if there are logs older than 24h. If not, will fetch all logs. Need a flag to not keep doing that over an over.
    @Query("SELECT COUNT(*) == 0 FROM LogEntry WHERE timestamp >= :past24hours")
    fun availableLogsOlderThan24h(past24hours: Long): Int
}
