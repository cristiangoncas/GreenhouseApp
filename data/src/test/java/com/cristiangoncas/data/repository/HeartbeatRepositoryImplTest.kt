package com.cristiangoncas.data.repository

import com.cristiangoncas.data.remote.RemoteDataSource
import com.cristiangoncas.greenhousemonitor.domain.models.CustomResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HeartbeatRepositoryImplTest {

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    private lateinit var repository: HeartbeatRepository

    @Before
    fun setUp() {
        repository = HeartbeatRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `Fetch nextHeartBeat`() = runTest {
        // Given
        // When
        `when`(remoteDataSource.nextHeartBeat())
            .thenReturn(CustomResult.Success(mock()))
        val nextHeartBeat = repository.nextHeartBeat().first()
        // Then
        assertTrue(nextHeartBeat.isSuccessful())
        assertTrue(nextHeartBeat is CustomResult.Success)

        `when`(remoteDataSource.nextHeartBeat())
            .thenReturn(CustomResult.Error("Error"))
        val nextHeartBeatError = repository.nextHeartBeat().first()
        // Then
        assertFalse(nextHeartBeatError.isSuccessful())
        assertTrue(nextHeartBeatError is CustomResult.Error)
    }

    @Test
    fun `Set max temp`() = runTest {
        // Given
        // When
        `when`(remoteDataSource.setMaxTemp(25))
            .thenReturn(CustomResult.Success(Unit))
        val setMaxTemp = repository.setMaxTemp(25).first()
        // Then
        assertTrue(setMaxTemp.isSuccessful())
        assertTrue(setMaxTemp is CustomResult.Success)

        `when`(remoteDataSource.setMaxTemp(25))
            .thenReturn(CustomResult.Error("Error"))
        val setMaxTempError = repository.setMaxTemp(25).first()
        // Then
        assertFalse(setMaxTempError.isSuccessful())
        assertTrue(setMaxTempError is CustomResult.Error)
    }

    @Test
    fun `Set min temp`() = runTest {
        // Given
        // When
        `when`(remoteDataSource.setMinTemp(17))
            .thenReturn(CustomResult.Success(Unit))
        val setMinTemp = repository.setMinTemp(17).first()
        // Then
        assertTrue(setMinTemp.isSuccessful())
        assertTrue(setMinTemp is CustomResult.Success)

        `when`(remoteDataSource.setMinTemp(17))
            .thenReturn(CustomResult.Error("Error"))
        val setMinTempError = repository.setMinTemp(17).first()
        // Then
        assertFalse(setMinTempError.isSuccessful())
        assertTrue(setMinTempError is CustomResult.Error)
    }

    @Test
    fun `Set morning time`() = runTest {
        // Given
        // When
        `when`(remoteDataSource.setMorningTime(7))
            .thenReturn(CustomResult.Success(Unit))
        val setMorningTime = repository.setMorningTime(7).first()
        // Then
        assertTrue(setMorningTime.isSuccessful())
        assertTrue(setMorningTime is CustomResult.Success)

        `when`(remoteDataSource.setMorningTime(7))
            .thenReturn(CustomResult.Error("Error"))
        val setMorningTimeError = repository.setMorningTime(7).first()
        // Then
        assertFalse(setMorningTimeError.isSuccessful())
        assertTrue(setMorningTimeError is CustomResult.Error)
    }

    @Test
    fun `Set night time`() = runTest {
        // Given
        // When
        `when`(remoteDataSource.setNightTime(19))
            .thenReturn(CustomResult.Success(Unit))
        val setNightTime = repository.setNightTime(19).first()
        // Then
        assertTrue(setNightTime.isSuccessful())
        assertTrue(setNightTime is CustomResult.Success)

        `when`(remoteDataSource.setNightTime(19))
            .thenReturn(CustomResult.Error("Error"))
        val setNightTimeError = repository.setNightTime(19).first()
        // Then
        assertFalse(setNightTimeError.isSuccessful())
        assertTrue(setNightTimeError is CustomResult.Error)
    }

    @Test
    fun `Set night temp difference`() = runTest {
        // Given
        // When
        `when`(remoteDataSource.setNightTempDifference(5))
            .thenReturn(CustomResult.Success(Unit))
        val setNightTempDifference = repository.setNightTempDifference(5).first()
        // Then
        assertTrue(setNightTempDifference.isSuccessful())
        assertTrue(setNightTempDifference is CustomResult.Success)

        `when`(remoteDataSource.setNightTempDifference(5))
            .thenReturn(CustomResult.Error("Error"))
        val setNightTempDifferenceError = repository.setNightTempDifference(5).first()
        // Then
        assertFalse(setNightTempDifferenceError.isSuccessful())
        assertTrue(setNightTempDifferenceError is CustomResult.Error)
    }

    @Test
    fun `Set health check`() = runTest {
        // Given
        // When
        `when`(remoteDataSource.setHealthCheck())
            .thenReturn(CustomResult.Success(Unit))
        val setHealthCheck = repository.setHealthCheck().first()
        // Then
        assertTrue(setHealthCheck.isSuccessful())
        assertTrue(setHealthCheck is CustomResult.Success)

        `when`(remoteDataSource.setHealthCheck())
            .thenReturn(CustomResult.Error("Error"))
        val setHealthCheckError = repository.setHealthCheck().first()
        // Then
        assertFalse(setHealthCheckError.isSuccessful())
        assertTrue(setHealthCheckError is CustomResult.Error)
    }

    @Test
    fun `Reset defaults`() = runTest {
        // Given
        // When
        `when`(remoteDataSource.resetDefaults())
            .thenReturn(CustomResult.Success(Unit))
        val resetDefaults = repository.resetDefaults().first()
        // Then
        assertTrue(resetDefaults.isSuccessful())
        assertTrue(resetDefaults is CustomResult.Success)

        `when`(remoteDataSource.resetDefaults())
            .thenReturn(CustomResult.Error("Error"))
        val resetDefaultsError = repository.resetDefaults().first()
        // Then
        assertFalse(resetDefaultsError.isSuccessful())
        assertTrue(resetDefaultsError is CustomResult.Error)
    }

    @Test
    fun `Set heartbeat period`() = runTest {
        // Given
        // When
        `when`(remoteDataSource.setHeartbeatPeriod(10))
            .thenReturn(CustomResult.Success(Unit))
        val setHeartbeatPeriod = repository.setHeartbeatPeriod(10).first()
        // Then
        assertTrue(setHeartbeatPeriod.isSuccessful())
        assertTrue(setHeartbeatPeriod is CustomResult.Success)

        `when`(remoteDataSource.setHeartbeatPeriod(10))
            .thenReturn(CustomResult.Error("Error"))
        val setHeartbeatPeriodError = repository.setHeartbeatPeriod(10).first()
        // Then
        assertFalse(setHeartbeatPeriodError.isSuccessful())
        assertTrue(setHeartbeatPeriodError is CustomResult.Error)
    }
}