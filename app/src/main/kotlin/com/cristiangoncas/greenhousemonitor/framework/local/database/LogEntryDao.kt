package com.cristiangoncas.greenhousemonitor.framework.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cristiangoncas.greenhousemonitor.framework.local.model.DbLogEntry
import com.cristiangoncas.greenhousemonitor.domain.models.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.domain.models.HeaterOnOffCounts
import com.cristiangoncas.greenhousemonitor.framework.local.model.DbAverageTempHumid
import com.cristiangoncas.greenhousemonitor.framework.local.model.DbHeaterOnOffCounts
import kotlinx.coroutines.flow.Flow

@Dao
interface LogEntryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLogEntries(logEntries: List<DbLogEntry>)

    @Query("SELECT id FROM DbLogEntry ORDER BY id DESC LIMIT 1")
    fun fetchLastLogEntryId(): Long

    @Query("SELECT * FROM DbLogEntry ORDER BY id DESC")
    fun fetchAllLogs(): Flow<List<DbLogEntry>>

    @Query("SELECT * FROM DbLogEntry ORDER BY id DESC LIMIT 250")
    fun fetchLatestLogEntriesFlow(): Flow<List<DbLogEntry>>

    @Query("SELECT \n" +
            "    ROUND(AVG(CASE WHEN event = 'tempRead' THEN CAST(REPLACE(data, '.', '') AS REAL) / 100.0 END), 2) AS avgTempRead,\n" +
            "    ROUND(AVG(CASE WHEN event = 'humidRead' THEN CAST(REPLACE(data, '.', '') AS REAL) / 100.0 END), 2) AS avgHumidRead\n" +
            "FROM DbLogEntry \n" +
            "WHERE (event = 'tempRead' OR event = 'humidRead') \n" +
            "AND timestamp >= :period")
    fun fetchAverageTempByPeriodOfTime(period: Long): Flow<DbAverageTempHumid>

    @Query("SELECT " +
            "SUM(CASE WHEN event = :event AND data = 'On' THEN 1 ELSE 0 END) AS heaterOnCount, " +
            "SUM(CASE WHEN event = :event AND data = 'Off' THEN 1 ELSE 0 END) AS heaterOffCount " +
            "FROM DbLogEntry " +
            "WHERE event = :event AND timestamp >= :period")
    fun fetchEventsByPeriodOfTime(event: String, period: Long): Flow<DbHeaterOnOffCounts>

    // Method to count the amount of logs. Will be used to do a fetch all logs if empty.
    @Query("SELECT COUNT(*) == 0 FROM DbLogEntry")
    fun areLogsEmpty(): Int

    // Method to check if there are logs older than 24h. If not, will fetch all logs. Need a flag to not keep doing that over an over.
    @Query("SELECT COUNT(*) == 0 FROM DbLogEntry WHERE timestamp >= :past24hours")
    fun availableLogsOlderThan24h(past24hours: Long): Int
}
