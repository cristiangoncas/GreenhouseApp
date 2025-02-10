package com.cristiangoncas.greenhousemonitor.framework.local

import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.greenhousemonitor.domain.models.Event
import com.cristiangoncas.greenhousemonitor.domain.models.LogEntry
import com.cristiangoncas.greenhousemonitor.framework.local.database.GreenhouseDB
import com.cristiangoncas.greenhousemonitor.framework.local.database.LogEntryDao
import com.cristiangoncas.greenhousemonitor.framework.local.model.DbAverageTempHumid
import com.cristiangoncas.greenhousemonitor.framework.local.model.DbHeaterOnOffCounts
import com.cristiangoncas.greenhousemonitor.framework.local.model.DbLogEntry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor

@RunWith(MockitoJUnitRunner::class)
class RoomDataSourceTest {

    @Mock
    private lateinit var db: GreenhouseDB

    @Mock
    private lateinit var logEntryDao: LogEntryDao

    @Captor
    private lateinit var dbLogEntryCaptor: ArgumentCaptor<List<DbLogEntry>>

    private var roomDataSource: RoomDataSource? = null

    @Before
    fun setUp() {
        roomDataSource = RoomDataSource(logEntryDao)
    }

    @After
    fun tearDown() {
        db.close()
        roomDataSource = null
    }

    @Test
    fun `Test fetch last log entries`() = runTest {
        // Given
        val logs = listOf(
            DbLogEntry(
                id = 1,
                timestamp = 1630512000,
                date = "2021-09-01T12:00:00",
                time = "12:00:00",
                data = "25",
                event = Event.TEMP_READ.value
            ),
            DbLogEntry(
                id = 2,
                timestamp = 1630512000,
                date = "2021-09-01T12:00:00",
                time = "12:00:00",
                data = "25",
                event = Event.TEMP_READ.value
            )
        )
        // When

        `when`(logEntryDao.fetchLatestLogEntriesFlow())
            .thenReturn(flowOf(logs))
        // Then
        val mappedLogEntries = roomDataSource!!.fetchLastLogEntries().first()
        assert(mappedLogEntries is CustomResult.Success)
        assert((mappedLogEntries as CustomResult.Success).data.size == 2)
    }

    @Test
    fun `Test fetch averages by period of time`() = runTest {
        // Given
        val averageTempHumid = DbAverageTempHumid(
            avgTempRead = 25.0f,
            avgHumidRead = 50.0f
        )
        val period = 1000L
        // When
        `when`(logEntryDao.fetchAverageTempByPeriodOfTime(period))
            .thenReturn(flowOf(averageTempHumid))
        // Then
        val mappedAverageTempHumid = roomDataSource!!.fetchAveragesByPeriodOfTime(period).first()
        assert(mappedAverageTempHumid is CustomResult.Success)
        assert((mappedAverageTempHumid as CustomResult.Success).data.avgTempRead == 25.0f)
        assert(mappedAverageTempHumid.data.avgHumidRead == 50.0f)
    }

    @Test
    fun `Test fetch heater events by period of time`() = runTest {
        // Given
        val period = 1000L
        val heaterOnOffCounts = DbHeaterOnOffCounts(
            heaterOnCount = 1,
            heaterOffCount = 10
        )
        // When
        `when`(logEntryDao.fetchEventsByPeriodOfTime(anyString(), anyLong()))
            .thenReturn(flowOf(heaterOnOffCounts))
        // Then
        val mappedHeaterOnOffCounts = roomDataSource!!.fetchHeaterEventsByPeriodOfTime(period).first()
        assert(mappedHeaterOnOffCounts is CustomResult.Success)
        assert((mappedHeaterOnOffCounts as CustomResult.Success).data.heaterOnCount == 1)
        assert(mappedHeaterOnOffCounts.data.heaterOffCount == 10)
    }

    @Test
    fun `Test insert log entries`() = runTest {
        // Given
        val logEntries = listOf(
            LogEntry(
                id = 1,
                timestamp = 1630512000,
                date = "2021-09-01T12:00:00",
                time = "12:00:00",
                data = "25",
                event = Event.TEMP_READ.value
            ),
            LogEntry(
                id = 2,
                timestamp = 1630512000,
                date = "2021-09-01T12:00:00",
                time = "12:00:00",
                data = "25",
                event = Event.TEMP_READ.value
            )
        )
        // When
        // Then
        val result = roomDataSource!!.insertLogEntries(logEntries).first()
        assert(result is CustomResult.Success)
    }
}