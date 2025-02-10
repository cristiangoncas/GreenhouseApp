package com.cristiangoncas.data.repository

import com.cristiangoncas.data.local.LocalDataSource
import com.cristiangoncas.data.remote.ConnectivityDataSource
import com.cristiangoncas.data.remote.RemoteDataSource
import com.cristiangoncas.greenhousemonitor.domain.models.AverageTempHumid
import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.greenhousemonitor.domain.models.Event
import com.cristiangoncas.greenhousemonitor.domain.models.HeaterOnOffCounts
import com.cristiangoncas.greenhousemonitor.domain.models.LogEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.anyList
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LogsRepositoryImplTest {

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    @Mock
    private lateinit var localDataSource: LocalDataSource

    @Mock
    private lateinit var connectivityState: ConnectivityDataSource

    private lateinit var repository: LogsRepository

    @Before
    fun setUp() {
        repository = LogsRepositoryImpl(remoteDataSource, localDataSource, connectivityState)
    }

    @Test
    fun `Fetch last logs when local is empty`() = runTest {
    // Given
        val localLogs = emptyList<LogEntry>()
        val remoteLogs = CustomResult.Success(
            listOf(
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
        )
        // When
        val localLogsFlow = flowOf(CustomResult.Success(localLogs))
        `when`(localDataSource.fetchLastLogEntries())
            .thenReturn(localLogsFlow)
            .thenReturn(flowOf(remoteLogs))
        `when`(connectivityState.isConnected)
            .thenReturn(MutableStateFlow(true))
        // TODO: Change this line when the logic changes, see TODO at LogsRepository::fetchRemoteLogs

        `when`(remoteDataSource.getAllLogs())
            .thenReturn(remoteLogs)
        `when`(localDataSource.insertLogEntries(anyList()))
            .thenReturn(flow { emit(CustomResult.Success(Unit)) })
//        `when`(localDataSource.fetchLastLogEntries())

        // Then
        val lastLogs = repository.fetchLastLogEntries().first()
        assertTrue(lastLogs.isSuccessful())
        assertTrue((lastLogs as CustomResult.Success).data.isEmpty())
        val lastLogsAfterRemoteUpdate = repository.fetchLastLogEntries().first()
        assertTrue(lastLogsAfterRemoteUpdate.isSuccessful())
        assertTrue((lastLogsAfterRemoteUpdate as CustomResult.Success).data.isNotEmpty())
        assertEquals(remoteLogs, lastLogsAfterRemoteUpdate)
    }

    @Test
    fun `Fetch last logs when local is not up to date`() = runTest {
        // Given
        val localLogs = listOf(
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
        val remoteLogs = listOf(
                LogEntry(
                    id = 3,
                    timestamp = 1630512000,
                    date = "2021-09-01T12:00:00",
                    time = "12:00:00",
                    data = "25",
                    event = Event.TEMP_READ.value
                )
            )
        val updatedLocalLogs = localLogs + remoteLogs
        // When
        `when`(localDataSource.fetchLastLogEntries())
            .thenReturn(flowOf(CustomResult.Success(localLogs)))
            .thenReturn( flowOf(CustomResult.Success(updatedLocalLogs)))
        `when`(connectivityState.isConnected)
            .thenReturn(MutableStateFlow(true))
        // TODO: Change this line when the logic changes, see TODO at LogsRepository::fetchRemoteLogs
        `when`(remoteDataSource.getAllLogs())
            .thenReturn(CustomResult.Success(remoteLogs))
        `when`(localDataSource.insertLogEntries(anyList()))
            .thenReturn(flow { emit(CustomResult.Success(Unit)) })

        // Then
        val lastLogs = repository.fetchLastLogEntries().first()
        assertTrue(lastLogs.isSuccessful())
        lastLogs as CustomResult.Success
        assertEquals(localLogs, lastLogs.data)
        val updateLastLogs = repository.fetchLastLogEntries().first()
        assertTrue(updateLastLogs.isSuccessful())
        updateLastLogs as CustomResult.Success
        assertEquals(updatedLocalLogs, updateLastLogs.data)
    }

    @Test
    fun `Fetch averages by period of time`() = runTest {
        // Given
        val period = 1000L
        val averages = CustomResult.Success(
            AverageTempHumid(
                avgTempRead = 25.0f,
                avgHumidRead = 50.0f
            )
        )
        // When
        `when`(localDataSource.fetchAveragesByPeriodOfTime(period))
            .thenReturn(flowOf(averages))
            .thenReturn(flowOf(CustomResult.Error("Error")))
        // Then
        val averagesFlow = repository.fetchAveragesByPeriodOfTime(period)
        assertEquals(averages, averagesFlow.first())
        assertTrue(repository.fetchAveragesByPeriodOfTime(period).first() is CustomResult.Error)
    }

    @Test
    fun `Fetch heater events by period of time`() = runTest {
        // Given
        val period = 1000L
        val heaterEvents = CustomResult.Success(HeaterOnOffCounts(10, 5))
        // When
        `when`(localDataSource.fetchHeaterEventsByPeriodOfTime(period))
            .thenReturn(flowOf(heaterEvents))
            .thenReturn(flowOf(CustomResult.Error("Error")))
        // Then
        val heaterEventsFlow = repository.fetchHeaterEventsByPeriodOfTime(period)
        assertEquals(heaterEvents, heaterEventsFlow.first())
        assertTrue(repository.fetchHeaterEventsByPeriodOfTime(period).first() is CustomResult.Error)
    }
}
