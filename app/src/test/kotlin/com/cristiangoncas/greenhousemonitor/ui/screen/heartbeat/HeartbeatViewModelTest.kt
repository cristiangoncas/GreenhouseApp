package com.cristiangoncas.greenhousemonitor.ui.screen.heartbeat

import com.cristiangoncas.data.repository.HeartbeatRepository
import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import com.cristiangoncas.greenhousemonitor.domain.models.HeartBeat
import com.cristiangoncas.usecases.NextHeartbeatUseCase
import com.cristiangoncas.usecases.RequestHealthCheckUseCase
import com.cristiangoncas.usecases.ResetDefaultParamsUseCase
import com.cristiangoncas.usecases.SetHeartbeatPeriodUseCase
import com.cristiangoncas.usecases.SetMaxTempUseCase
import com.cristiangoncas.usecases.SetMinTempUseCase
import com.cristiangoncas.usecases.SetMorningTimeUseCase
import com.cristiangoncas.usecases.SetNightTempDifferenceUseCase
import com.cristiangoncas.usecases.SetNightTimeUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HeartbeatViewModelTest {

    @Mock
    private lateinit var repository: HeartbeatRepository

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var nextHeartbeatUseCase: NextHeartbeatUseCase
    private lateinit var setMaxTempUseCase: SetMaxTempUseCase
    private lateinit var setMinTempUseCase: SetMinTempUseCase
    private lateinit var setMorningTimeUseCase: SetMorningTimeUseCase
    private lateinit var setNightTempUseCase: SetNightTimeUseCase
    private lateinit var setNightTempDifferenceUseCase: SetNightTempDifferenceUseCase
    private lateinit var requestHealthCheckUseCase: RequestHealthCheckUseCase
    private lateinit var resetDefaultParamsUseCase: ResetDefaultParamsUseCase
    private lateinit var setHeartbeatPeriodUseCase: SetHeartbeatPeriodUseCase
    private lateinit var viewModel: HeartbeatViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        nextHeartbeatUseCase = NextHeartbeatUseCase(repository)
        setMaxTempUseCase = SetMaxTempUseCase(repository)
        setMinTempUseCase = SetMinTempUseCase(repository)
        setMorningTimeUseCase = SetMorningTimeUseCase(repository)
        setNightTempUseCase = SetNightTimeUseCase(repository)
        setNightTempDifferenceUseCase = SetNightTempDifferenceUseCase(repository)
        requestHealthCheckUseCase = RequestHealthCheckUseCase(repository)
        resetDefaultParamsUseCase = ResetDefaultParamsUseCase(repository)
        setHeartbeatPeriodUseCase = SetHeartbeatPeriodUseCase(repository)

        viewModel = HeartbeatViewModel(
            nextHeartbeatUseCase,
            setMaxTempUseCase,
            setMinTempUseCase,
            setMorningTimeUseCase,
            setNightTempUseCase,
            setNightTempDifferenceUseCase,
            requestHealthCheckUseCase,
            resetDefaultParamsUseCase,
            setHeartbeatPeriodUseCase
        )

        val nextHeartBeat = flowOf(
            CustomResult.Success(
                HeartBeat(
                    "25",
                    "17",
                    "7",
                    "19",
                    "5"
                )
            )
        )
        `when`(repository.nextHeartBeat())
            .thenReturn(nextHeartBeat)

        `when`(repository.setMaxTemp(anyInt()))
            .thenReturn(flowOf(CustomResult.Success(Unit)))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Test UiState when nextHeartbeatUseCase returns Success`() = runTest {
        // Given
        // When

        viewModel.onUiReady()
        runCurrent()
        // Then
        val states = viewModel.state.take(2).toList()
        val state1 = states[0]
        assertTrue(state1.errors.isEmpty())
        assertTrue(state1.loading)
        assertTrue(state1.heartBeat.allEmpty())
        val state2 = states[1]
        assertTrue(state1.errors.isEmpty())
        assertFalse(state2.loading)
        assertFalse(state2.heartBeat.allEmpty())
    }

    @Test
    fun `Test UiState when nextHeartbeatUseCase returns Error`() = runTest {
        // Given
        val nextHeartBeat = flowOf(
            CustomResult.Error("Error")
        )
        `when`(repository.nextHeartBeat())
            .thenReturn(nextHeartBeat)
        // When

        viewModel.onUiReady()
        runCurrent()
        // Then
        val states = viewModel.state.take(2).toList()
        val state1 = states[0]
        assertTrue(state1.errors.isEmpty())
        assertTrue(state1.loading)
        assertTrue(state1.heartBeat.allEmpty())
        val state2 = states[1]
        assertEquals(1, state2.errors.size)
        assertFalse(state2.loading)
        assertTrue(state2.heartBeat.allEmpty())
    }

    @Test
    fun `Test setMaxTempUseCase`() = runTest {
        // Given
        val maxTemp = 20
        val setMaxTemp = flowOf(
            CustomResult.Success(Unit)
        )
        `when`(repository.setMaxTemp(maxTemp))
            .thenReturn(setMaxTemp)
        // When
//        viewModel.onUiReady()
        viewModel.setMaxTemp(maxTemp.toString())
        runCurrent()
        // Then
        val states = viewModel.state.take(2).toList()
        val state1 = states[0]
        assertTrue(state1.loading)
        assertTrue(state1.errors.isEmpty())
        assertTrue(state1.heartBeat.allEmpty())
        val state2 = states[1]
        assertTrue(state2.errors.isEmpty())
        assertFalse(state2.loading)
        assertFalse(state2.heartBeat.allEmpty())
//        val state3 = states[2]
//        assertTrue(state3.errors.isEmpty())
//        assertFalse(state3.loading)
//        assertFalse(state3.heartBeat.allEmpty())
    }
}
