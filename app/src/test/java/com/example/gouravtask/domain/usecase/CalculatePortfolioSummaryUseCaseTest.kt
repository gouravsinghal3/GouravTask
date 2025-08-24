package com.example.gouravtask.domain.usecase

import com.example.gouravtask.presentation.model.PortfolioSummary
import com.example.gouravtask.presentation.model.UiHolding
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CalculatePortfolioSummaryUseCaseTest {

    private lateinit var useCase: CalculatePortfolioSummaryUseCase

    @Before
    fun setUp() {
        useCase = CalculatePortfolioSummaryUseCase()
    }

    @Test
    fun `invoke should calculate portfolio summary for single holding`() {
        // Given
        val holdings = listOf(
            UiHolding("AAPL", 100, 150.0, 140.0, 145.0, null)
        )

        // Expected calculations:
        // currentValue = 150.0 * 100 = 15000.0
        // totalInvestment = 140.0 * 100 = 14000.0
        // todayPnl = (145.0 - 150.0) * 100 = -500.0
        // totalPnl = 15000.0 - 14000.0 = 1000.0

        // When
        val result = useCase(holdings)

        // Then
        assertNotNull(result)
        assertEquals(15000.0, result.currentValue, 0.01)
        assertEquals(14000.0, result.totalInvestment, 0.01)
        assertEquals(-500.0, result.todaysPnl, 0.01)
        assertEquals(1000.0, result.totalPnl, 0.01)
    }

    @Test
    fun `invoke should calculate portfolio summary for multiple holdings`() {
        // Given
        val holdings = listOf(
            UiHolding("AAPL", 100, 150.0, 140.0, 145.0, null),
            UiHolding("GOOGL", 50, 2800.0, 2750.0, 2780.0, null),
            UiHolding("MSFT", 200, 300.0, 290.0, 295.0, null)
        )

        // Expected calculations:
        // AAPL: currentValue = 150.0 * 100 = 15000.0, investment = 140.0 * 100 = 14000.0, todayPnl = (145.0 - 150.0) * 100 = -500.0
        // GOOGL: currentValue = 2800.0 * 50 = 140000.0, investment = 2750.0 * 50 = 137500.0, todayPnl = (2780.0 - 2800.0) * 50 = -1000.0
        // MSFT: currentValue = 300.0 * 200 = 60000.0, investment = 290.0 * 200 = 58000.0, todayPnl = (295.0 - 300.0) * 200 = -1000.0
        // Total: currentValue = 15000.0 + 140000.0 + 60000.0 = 215000.0
        // Total: investment = 14000.0 + 137500.0 + 58000.0 = 209500.0
        // Total: todayPnl = -500.0 + -1000.0 + -1000.0 = -2500.0
        // Total: totalPnl = 215000.0 - 209500.0 = 5500.0

        // When
        val result = useCase(holdings)

        // Then
        assertNotNull(result)
        assertEquals(215000.0, result.currentValue, 0.01)
        assertEquals(209500.0, result.totalInvestment, 0.01)
        assertEquals(-2500.0, result.todaysPnl, 0.01)
        assertEquals(5500.0, result.totalPnl, 0.01)
    }

    @Test
    fun `invoke should handle empty holdings list`() {
        // Given
        val holdings = emptyList<UiHolding>()

        // When
        val result = useCase(holdings)

        // Then
        assertNotNull(result)
        assertEquals(0.0, result.currentValue, 0.01)
        assertEquals(0.0, result.totalInvestment, 0.01)
        assertEquals(0.0, result.todaysPnl, 0.01)
        assertEquals(0.0, result.totalPnl, 0.01)
    }

    @Test
    fun `invoke should handle holdings with zero quantity`() {
        // Given
        val holdings = listOf(
            UiHolding("AAPL", 0, 150.0, 140.0, 145.0, null),
            UiHolding("GOOGL", 0, 2800.0, 2750.0, 2780.0, null)
        )

        // When
        val result = useCase(holdings)

        // Then
        assertNotNull(result)
        assertEquals(0.0, result.currentValue, 0.01)
        assertEquals(0.0, result.totalInvestment, 0.01)
        assertEquals(0.0, result.todaysPnl, 0.01)
        assertEquals(0.0, result.totalPnl, 0.01)
    }

    @Test
    fun `invoke should handle holdings with zero prices`() {
        // Given
        val holdings = listOf(
            UiHolding("AAPL", 100, 0.0, 0.0, 0.0, null),
            UiHolding("GOOGL", 50, 0.0, 0.0, 0.0, null)
        )

        // When
        val result = useCase(holdings)

        // Then
        assertNotNull(result)
        assertEquals(0.0, result.currentValue, 0.01)
        assertEquals(0.0, result.totalInvestment, 0.01)
        assertEquals(0.0, result.todaysPnl, 0.01)
        assertEquals(0.0, result.totalPnl, 0.01)
    }

    @Test
    fun `invoke should handle negative quantities`() {
        // Given
        val holdings = listOf(
            UiHolding("AAPL", -100, 150.0, 140.0, 145.0, null)
        )

        // Expected calculations:
        // currentValue = 150.0 * (-100) = -15000.0
        // totalInvestment = 140.0 * (-100) = -14000.0
        // todayPnl = (145.0 - 150.0) * (-100) = 500.0
        // totalPnl = -15000.0 - (-14000.0) = -1000.0

        // When
        val result = useCase(holdings)

        // Then
        assertNotNull(result)
        assertEquals(-15000.0, result.currentValue, 0.01)
        assertEquals(-14000.0, result.totalInvestment, 0.01)
        assertEquals(500.0, result.todaysPnl, 0.01)
        assertEquals(-1000.0, result.totalPnl, 0.01)
    }

    @Test
    fun `invoke should handle negative prices`() {
        // Given
        val holdings = listOf(
            UiHolding("AAPL", 100, -150.0, -140.0, -145.0, null)
        )

        // Expected calculations:
        // currentValue = -150.0 * 100 = -15000.0
        // totalInvestment = -140.0 * 100 = -14000.0
        // todayPnl = (-145.0 - (-150.0)) * 100 = 500.0
        // totalPnl = -15000.0 - (-14000.0) = -1000.0

        // When
        val result = useCase(holdings)

        // Then
        assertNotNull(result)
        assertEquals(-15000.0, result.currentValue, 0.01)
        assertEquals(-14000.0, result.totalInvestment, 0.01)
        assertEquals(500.0, result.todaysPnl, 0.01)
        assertEquals(-1000.0, result.totalPnl, 0.01)
    }

    @Test
    fun `invoke should handle very large numbers`() {
        // Given
        val holdings = listOf(
            UiHolding("AAPL", 1000000, 999999.99, 999999.98, 999999.97, null)
        )

        // Expected calculations:
        // currentValue = 999999.99 * 1000000 = 999999990000.0
        // totalInvestment = 999999.98 * 1000000 = 999999980000.0
        // todayPnl = (999999.97 - 999999.99) * 1000000 = -20000.0
        // totalPnl = 999999990000.0 - 999999980000.0 = 10000.0

        // When
        val result = useCase(holdings)

        // Then
        assertNotNull(result)
        assertEquals(999999990000.0, result.currentValue, 0.01)
        assertEquals(999999980000.0, result.totalInvestment, 0.01)
        assertEquals(-20000.0, result.todaysPnl, 0.01)
        assertEquals(10000.0, result.totalPnl, 0.01)
    }

    @Test
    fun `invoke should handle very small numbers`() {
        // Given
        val holdings = listOf(
            UiHolding("AAPL", 1, 0.0001, 0.0002, 0.0003, null)
        )

        // Expected calculations:
        // currentValue = 0.0001 * 1 = 0.0001
        // totalInvestment = 0.0002 * 1 = 0.0002
        // todayPnl = (0.0003 - 0.0001) * 1 = 0.0002
        // totalPnl = 0.0001 - 0.0002 = -0.0001

        // When
        val result = useCase(holdings)

        // Then
        assertNotNull(result)
        assertEquals(0.0001, result.currentValue, 0.000001)
        assertEquals(0.0002, result.totalInvestment, 0.000001)
        assertEquals(0.0002, result.todaysPnl, 0.000001)
        assertEquals(-0.0001, result.totalPnl, 0.000001)
    }

    @Test
    fun `invoke should handle mixed positive and negative values`() {
        // Given
        val holdings = listOf(
            UiHolding("AAPL", 100, 150.0, 140.0, 145.0, null),
            UiHolding("GOOGL", -50, 2800.0, 2750.0, 2780.0, null)
        )

        // Expected calculations:
        // AAPL: currentValue = 150.0 * 100 = 15000.0, investment = 140.0 * 100 = 14000.0, todayPnl = (145.0 - 150.0) * 100 = -500.0
        // GOOGL: currentValue = 2800.0 * (-50) = -140000.0, investment = 2750.0 * (-50) = -137500.0, todayPnl = (2780.0 - 2800.0) * (-50) = 1000.0
        // Total: currentValue = 15000.0 + (-140000.0) = -125000.0
        // Total: investment = 14000.0 + (-137500.0) = -123500.0
        // Total: todayPnl = -500.0 + 1000.0 = 500.0
        // Total: totalPnl = -125000.0 - (-123500.0) = -1500.0

        // When
        val result = useCase(holdings)

        // Then
        assertNotNull(result)
        assertEquals(-125000.0, result.currentValue, 0.01)
        assertEquals(-123500.0, result.totalInvestment, 0.01)
        assertEquals(500.0, result.todaysPnl, 0.01)
        assertEquals(-1500.0, result.totalPnl, 0.01)
    }

    @Test
    fun `invoke should handle holdings with same close and ltp prices`() {
        // Given
        val holdings = listOf(
            UiHolding("AAPL", 100, 150.0, 140.0, 150.0, null)
        )

        // Expected calculations:
        // currentValue = 150.0 * 100 = 15000.0
        // totalInvestment = 140.0 * 100 = 14000.0
        // todayPnl = (150.0 - 150.0) * 100 = 0.0
        // totalPnl = 15000.0 - 14000.0 = 1000.0

        // When
        val result = useCase(holdings)

        // Then
        assertNotNull(result)
        assertEquals(15000.0, result.currentValue, 0.01)
        assertEquals(14000.0, result.totalInvestment, 0.01)
        assertEquals(0.0, result.todaysPnl, 0.01)
        assertEquals(1000.0, result.totalPnl, 0.01)
    }

    @Test
    fun `invoke should handle holdings with same avgPrice and ltp prices`() {
        // Given
        val holdings = listOf(
            UiHolding("AAPL", 100, 150.0, 150.0, 145.0, null)
        )

        // Expected calculations:
        // currentValue = 150.0 * 100 = 15000.0
        // totalInvestment = 150.0 * 100 = 15000.0
        // todayPnl = (145.0 - 150.0) * 100 = -500.0
        // totalPnl = 15000.0 - 15000.0 = 0.0

        // When
        val result = useCase(holdings)

        // Then
        assertNotNull(result)
        assertEquals(15000.0, result.currentValue, 0.01)
        assertEquals(15000.0, result.totalInvestment, 0.01)
        assertEquals(-500.0, result.todaysPnl, 0.01)
        assertEquals(0.0, result.totalPnl, 0.01)
    }

    @Test
    fun `invoke should handle holdings with all same prices`() {
        // Given
        val holdings = listOf(
            UiHolding("AAPL", 100, 150.0, 150.0, 150.0, null)
        )

        // Expected calculations:
        // currentValue = 150.0 * 100 = 15000.0
        // totalInvestment = 150.0 * 100 = 15000.0
        // todayPnl = (150.0 - 150.0) * 100 = 0.0
        // totalPnl = 15000.0 - 15000.0 = 0.0

        // When
        val result = useCase(holdings)

        // Then
        assertNotNull(result)
        assertEquals(15000.0, result.currentValue, 0.01)
        assertEquals(15000.0, result.totalInvestment, 0.01)
        assertEquals(0.0, result.todaysPnl, 0.01)
        assertEquals(0.0, result.totalPnl, 0.01)
    }

    @Test
    fun `invoke should return PortfolioSummary object`() {
        // Given
        val holdings = listOf(
            UiHolding("AAPL", 100, 150.0, 140.0, 145.0, null)
        )

        // When
        val result = useCase(holdings)

        // Then
        assertNotNull(result)
        assert(result is PortfolioSummary)
    }

    @Test
    fun `invoke should handle null error field in UiHolding`() {
        // Given
        val holdings = listOf(
            UiHolding("AAPL", 100, 150.0, 140.0, 145.0, null)
        )

        // When
        val result = useCase(holdings)

        // Then
        assertNotNull(result)
        // Should not crash and should calculate correctly
        assertEquals(15000.0, result.currentValue, 0.01)
    }

    @Test
    fun `invoke should handle non-null error field in UiHolding`() {
        // Given
        val holdings = listOf(
            UiHolding("AAPL", 100, 150.0, 140.0, 145.0, "Some error")
        )

        // When
        val result = useCase(holdings)

        // Then
        assertNotNull(result)
        // Should not crash and should calculate correctly regardless of error field
        assertEquals(15000.0, result.currentValue, 0.01)
    }
}