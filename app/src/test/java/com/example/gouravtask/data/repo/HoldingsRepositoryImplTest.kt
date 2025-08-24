package com.example.gouravtask.data.repo

import com.example.gouravtask.data.api.ApiService
import com.example.gouravtask.data.db.HoldingsDao
import com.example.gouravtask.data.db.entity.Holding
import com.example.gouravtask.data.db.entity.NetworkHolding
import com.example.gouravtask.presentation.model.HoldingsResponse
import com.example.gouravtask.presentation.model.Data
import com.example.gouravtask.presentation.model.UiHolding
import com.example.gouravtask.presentation.model.toUiHolding
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.net.UnknownHostException
import java.net.SocketTimeoutException
import retrofit2.HttpException

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class HoldingsRepositoryImplTest {

    private lateinit var repository: HoldingsRepositoryImpl
    private lateinit var apiService: ApiService
    private lateinit var holdingsDao: HoldingsDao

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        apiService = mockk<ApiService>(relaxed = true)
        holdingsDao = mockk<HoldingsDao>(relaxed = true)
        repository = HoldingsRepositoryImpl(apiService, holdingsDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getHoldings should emit cached data first`() = runTest {
        // Given
        val cachedHoldings = listOf(
            Holding("AAPL", 100, 150.0, 140.0, 145.0),
            Holding("GOOGL", 50, 2800.0, 2750.0, 2780.0)
        )
        val expectedUiHoldings = cachedHoldings.map { it.toUiHolding() }

        every { holdingsDao.getAllHoldings() } returns flowOf(cachedHoldings)
        coEvery { apiService.getHoldings() } returns mockk<HoldingsResponse>(relaxed = true)

        // When
        val result = repository.getHoldings()
        val emittedData = mutableListOf<List<UiHolding>>()

        result.take(2).collect {
            emittedData.add(it)
        }

        // Then
        assertEquals(2, emittedData.size)
        assertEquals(expectedUiHoldings, emittedData[0]) // First emission should be cached data
    }

    @Test
    fun `getHoldings should fetch from network and update cache`() = runTest {
        // Given
        val cachedHoldings = listOf(Holding("AAPL", 100, 150.0, 140.0, 145.0))
        val networkHoldings = listOf(
            NetworkHolding("AAPL", 100, 150.0, 140.0, 145.0),
            NetworkHolding("GOOGL", 50, 2800.0, 2750.0, 2780.0)
        )
        val networkResponse = HoldingsResponse(Data(networkHoldings))

        every { holdingsDao.getAllHoldings() } returns flowOf(cachedHoldings)
        coEvery { apiService.getHoldings() } returns networkResponse
        coEvery { holdingsDao.clearAll() } returns Unit
        coEvery { holdingsDao.insertAll(any()) } returns Unit

        // When
        val result = repository.getHoldings()
        val emittedData = mutableListOf<List<UiHolding>>()

        result.take(3).collect {
            emittedData.add(it)
        }

        // Then
        coVerify {
            holdingsDao.clearAll()
            holdingsDao.insertAll(any())
        }
        assertTrue(emittedData.size >= 2)
    }

    @Test
    fun `getHoldings should handle network errors gracefully`() = runTest {
        // Given
        val cachedHoldings = listOf(Holding("AAPL", 100, 150.0, 140.0, 145.0))
        val networkError = UnknownHostException("No internet connection")

        every { holdingsDao.getAllHoldings() } returns flowOf(cachedHoldings)
        coEvery { apiService.getHoldings() } throws networkError

        // When
        val result = repository.getHoldings()
        val emittedData = mutableListOf<List<UiHolding>>()

        result.take(2).collect {
            emittedData.add(it)
        }

        // Then
        assertEquals(2, emittedData.size)
        assertEquals(cachedHoldings.map { it.toUiHolding() }, emittedData[0])
        // Should still emit cached data even after network error
    }

    @Test
    fun `getHoldings should handle HTTP errors`() = runTest {
        // Given
        val cachedHoldings = listOf(Holding("AAPL", 100, 150.0, 140.0, 145.0))
        val httpError = HttpException(mockk(relaxed = true))

        every { holdingsDao.getAllHoldings() } returns flowOf(cachedHoldings)
        coEvery { apiService.getHoldings() } throws httpError

        // When
        val result = repository.getHoldings()
        val emittedData = mutableListOf<List<UiHolding>>()

        result.take(2).collect {
            emittedData.add(it)
        }

        // Then
        assertEquals(2, emittedData.size)
        // Should still emit cached data after HTTP error
    }

    @Test
    fun `getHoldings should handle timeout errors`() = runTest {
        // Given
        val cachedHoldings = listOf(Holding("AAPL", 100, 150.0, 140.0, 145.0))
        val timeoutError = SocketTimeoutException("Request timeout")

        every { holdingsDao.getAllHoldings() } returns flowOf(cachedHoldings)
        coEvery { apiService.getHoldings() } throws timeoutError

        // When
        val result = repository.getHoldings()
        val emittedData = mutableListOf<List<UiHolding>>()

        result.take(2).collect {
            emittedData.add(it)
        }

        // Then
        assertEquals(2, emittedData.size)
        // Should still emit cached data after timeout error
    }

    @Test
    fun `getHoldings should handle generic exceptions`() = runTest {
        // Given
        val cachedHoldings = listOf(Holding("AAPL", 100, 150.0, 140.0, 145.0))
        val genericError = RuntimeException("Unknown error")

        every { holdingsDao.getAllHoldings() } returns flowOf(cachedHoldings)
        coEvery { apiService.getHoldings() } throws genericError

        // When
        val result = repository.getHoldings()
        val emittedData = mutableListOf<List<UiHolding>>()

        result.take(2).collect {
            emittedData.add(it)
        }

        // Then
        assertEquals(2, emittedData.size)
        // Should still emit cached data after generic error
    }

    @Test
    fun `getHoldings should emit database flow after network update`() = runTest {
        // Given
        val cachedHoldings = listOf(Holding("AAPL", 100, 150.0, 140.0, 145.0))
        val updatedHoldings = listOf(
            Holding("AAPL", 100, 150.0, 140.0, 145.0),
            Holding("GOOGL", 50, 2800.0, 2750.0, 2780.0)
        )
        val networkResponse = HoldingsResponse(Data(listOf(
            NetworkHolding("AAPL", 100, 150.0, 140.0, 145.0),
            NetworkHolding("GOOGL", 50, 2800.0, 2750.0, 2780.0)
        )))

        every { holdingsDao.getAllHoldings() } returns flowOf(cachedHoldings, updatedHoldings)
        coEvery { apiService.getHoldings() } returns networkResponse
        coEvery { holdingsDao.clearAll() } returns Unit
        coEvery { holdingsDao.insertAll(any()) } returns Unit

        // When
        val result = repository.getHoldings()
        val emittedData = mutableListOf<List<UiHolding>>()

        result.take(3).collect {
            emittedData.add(it)
        }

        // Then
        assertTrue(emittedData.size >= 3)
        assertEquals(cachedHoldings.map { it.toUiHolding() }, emittedData[0])
        assertEquals(updatedHoldings.map { it.toUiHolding() }, emittedData[2])
    }
}