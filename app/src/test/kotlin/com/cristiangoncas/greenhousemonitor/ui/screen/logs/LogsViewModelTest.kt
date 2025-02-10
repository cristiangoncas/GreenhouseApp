package com.cristiangoncas.greenhousemonitor.ui.screen.logs

import com.cristiangoncas.data.repository.LogsRepository
import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.greenhousemonitor.domain.models.LogEntry
import com.cristiangoncas.usecases.FetchLastLogsUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LogsViewModelTest {

    @Mock
    private lateinit var repository: LogsRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Test on UI ready when logs not empty`() = runTest {
        val logs = listOf(
            LogEntry(
                id = 1,
                timestamp = 1630512000,
                date = "2021-09-01T12:00:00",
                time = "12:00:00",
                data = "25",
                event = "TEMP_READ"
            ),
            LogEntry(
                id = 2,
                timestamp = 1630512000,
                date = "2021-09-01T12:00:00",
                time = "12:00:00",
                data = "25",
                event = "TEMP_READ"
            )
        )
        val lastLogsFlow: Flow<CustomResult<List<LogEntry>>> =
            flowOf(CustomResult.Success(logs))

        `when`(repository.fetchLastLogEntries())
            .thenReturn(lastLogsFlow)

        val viewModel = LogsViewModel(FetchLastLogsUseCase(repository))

        viewModel.onUiReady()

        runCurrent()

        val states = viewModel.state.take(2).toList()
        val state1 = states[0]
        assertNull(state1.error)
        assertTrue(state1.loading)
        assertTrue(state1.logs.isEmpty())
        val state2 = states[1]
        assertTrue(state2.logs.isNotEmpty())
        assertFalse(state2.loading)
    }

    @Test
    fun `Test on UI ready when use case returns Error`() = runTest {
        val lastLogsFlowError: Flow<CustomResult.Error> =
            flowOf(CustomResult.Error("Error"))

        `when`(repository.fetchLastLogEntries())
            .thenReturn(lastLogsFlowError)

        val viewModel = LogsViewModel(FetchLastLogsUseCase(repository))

        viewModel.onUiReady()

        runCurrent()

        val states = viewModel.state.take(2).toList()
        val state1 = states[0]
        assertNull(state1.error)
        assertTrue(state1.loading)
        assertTrue(state1.logs.isEmpty())
        val state2 = states[1]
        assertNotNull(state2.error)
        assertEquals("Error", state2.error)
        assertTrue(state2.logs.isEmpty())
        assertFalse(state2.loading)
    }

    @Test
    fun `Test refresh`() = runTest {
        val logs = listOf(
            LogEntry(
                id = 1,
                timestamp = 1630512000,
                date = "2021-09-01T12:00:00",
                time = "12:00:00",
                data = "25",
                event = "TEMP_READ"
            ),
            LogEntry(
                id = 2,
                timestamp = 1630512000,
                date = "2021-09-01T12:00:00",
                time = "12:00:00",
                data = "25",
                event = "TEMP_READ"
            )
        )
        val lastLogsFlow: Flow<CustomResult<List<LogEntry>>> =
            flowOf(CustomResult.Success(logs))

        `when`(repository.fetchLastLogEntries())
            .thenReturn(lastLogsFlow)

        val viewModel = LogsViewModel(FetchLastLogsUseCase(repository))

        viewModel.refresh()

        runCurrent()

        val states = viewModel.state.take(2).toList()
        val state1 = states[0]
        assertNull(state1.error)
        assertTrue(state1.loading)
        assertTrue(state1.logs.isEmpty())
        val state2 = states[1]
        assertNull(state2.error)
        assertTrue(state2.logs.isNotEmpty())
        assertFalse(state2.loading)
    }
}
