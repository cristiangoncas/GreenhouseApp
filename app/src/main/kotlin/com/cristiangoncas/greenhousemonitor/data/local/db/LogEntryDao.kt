package com.cristiangoncas.greenhousemonitor.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cristiangoncas.greenhousemonitor.data.local.model.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.data.local.model.HeaterOnOffCounts
import com.cristiangoncas.greenhousemonitor.data.local.model.LogEntry
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

    @Query("SELECT \n" +
            "    ROUND(AVG(CASE WHEN event = 'tempRead' THEN CAST(REPLACE(data, '.', '') AS REAL) / 100.0 END), 2) AS avgTempRead,\n" +
            "    ROUND(AVG(CASE WHEN event = 'humidRead' THEN CAST(REPLACE(data, '.', '') AS REAL) / 100.0 END), 2) AS avgHumidRead\n" +
            "FROM LogEntry \n" +
            "WHERE (event = 'tempRead' OR event = 'humidRead') \n" +
            "AND timestamp >= :period")
    fun fetchAverageTempByPeriodOfTime(period: Long): Flow<AverageTempHumid>

    @Query("SELECT " +
            "SUM(CASE WHEN event = 'heater' AND data = 'On' THEN 1 ELSE 0 END) AS heaterOnCount, " +
            "SUM(CASE WHEN event = 'heater' AND data = 'Off' THEN 1 ELSE 0 END) AS heaterOffCount " +
            "FROM LogEntry " +
            "WHERE event = 'heater' AND timestamp >= :period")
    fun fetchEventsByPeriodOfTime(period: Long): Flow<HeaterOnOffCounts>

    // Method to count the amount of logs. Will be used to do a fetch all logs if empty.
    @Query("SELECT COUNT(*) == 0 FROM LogEntry")
    fun areLogsEmpty(): Int

    // Method to check if there are logs older than 24h. If not, will fetch all logs. Need a flag to not keep doing that over an over.
    @Query("SELECT COUNT(*) == 0 FROM LogEntry WHERE timestamp >= :past24hours")
    fun availableLogsOlderThan24h(past24hours: Long): Int
}
