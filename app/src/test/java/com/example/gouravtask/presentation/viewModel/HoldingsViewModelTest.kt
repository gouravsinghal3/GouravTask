package com.example.gouravtask.presentation.viewModel

import com.example.gouravtask.domain.usecase.CalculatePortfolioSummaryUseCase
import com.example.gouravtask.domain.usecase.GetHoldingsUseCase
import com.example.gouravtask.presentation.model.HoldingsResponseState
import com.example.gouravtask.presentation.model.PortfolioSummary
import com.example.gouravtask.presentation.model.UiHolding
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class HoldingsViewModelTest {

    private lateinit var viewModel: HoldingsViewModel
    private lateinit var mockGetHoldingsUseCase: GetHoldingsUseCase
    private lateinit var mockCalculatePortfolioSummaryUseCase: CalculatePortfolioSummaryUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        mockGetHoldingsUseCase = mockk(relaxed = true)
        mockCalculatePortfolioSummaryUseCase = mockk(relaxed = true)
        viewModel = HoldingsViewModel(mockGetHoldingsUseCase, mockCalculatePortfolioSummaryUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should call fetchHoldings`() = runTest {
        // Given
        every { mockGetHoldingsUseCase() } returns flow {
            emit(
                HoldingsResponseState.Success(
                    emptyList()
                )
            )
        }

        // When
        HoldingsViewModel(mockGetHoldingsUseCase, mockCalculatePortfolioSummaryUseCase)

        // Then
        verify { mockGetHoldingsUseCase() }
    }

    @Test
    fun `fetchHoldings should set loading to true initially`() = runTest {
        // Given
        every { mockGetHoldingsUseCase() } returns flow {
            emit(
                HoldingsResponseState.Success(
                    emptyList()
                )
            )
        }

        // When
        viewModel.fetchHoldings()

        // Then
        assert(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `fetchHoldings should update state with holdings data`() = runTest {
        // Given
        val testHoldings = listOf(
            UiHolding("AAPL", 100, 150.0, 140.0, 145.0, null),
            UiHolding("GOOGL", 50, 2800.0, 2750.0, 2780.0, null)
        )

        val testSummary = PortfolioSummary(
            currentValue = 10000.0,
            totalInvestment = 9500.0,
            todaysPnl = 500.0,
            totalPnl = 500.0
        )

        every { mockGetHoldingsUseCase() } returns flow {
            emit(
                HoldingsResponseState.Success(
                    testHoldings
                )
            )
        }
        every { mockCalculatePortfolioSummaryUseCase(testHoldings) } returns testSummary

        // When
        viewModel.fetchHoldings()

        // Then
        advanceUntilIdle()
        assert(viewModel.uiState.value.holdings == testHoldings)
        assert(viewModel.uiState.value.portfolioSummary == testSummary)
        assert(!viewModel.uiState.value.isLoading)
    }

    @Test
    fun `viewModel should be properly initialized`() {
        // Then
        assert(viewModel.uiState.value.holdings.isEmpty())
        assert(viewModel.uiState.value.portfolioSummary == null)
        assert(viewModel.uiState.value.error == null)
    }
}