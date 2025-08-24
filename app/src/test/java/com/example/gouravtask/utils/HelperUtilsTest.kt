package com.example.gouravtask.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HelperUtilsTest {

    @Test
    fun `getFormattedAmount should format zero amount correctly`() {
        // Given
        val amount = 0.0

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        assertEquals("₹0.00", result)
    }

    @Test
    fun `getFormattedAmount should format positive amount correctly`() {
        // Given
        val amount = 1234.56

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        assertEquals("₹1,234.56", result)
    }

    @Test
    fun `getFormattedAmount should format negative amount correctly`() {
        // Given
        val amount = -1234.56

        // When
        val result = HelperUtils.getFormattedAmount(amount, true)

        // Then
        assertEquals("-₹1,234.56", result)
    }

    @Test
    fun `getFormattedAmount should format large positive amount correctly`() {
        // Given
        val amount = 999999.99

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        assertEquals("₹9,99,999.99", result)
    }

    @Test
    fun `getFormattedAmount should format large negative amount correctly`() {
        // Given
        val amount = -999999.99

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        assertEquals("-₹9,99,999.99", result)
    }

    @Test
    fun `getFormattedAmount should format decimal amount correctly`() {
        // Given
        val amount = 0.01

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        assertEquals("₹0.01", result)
    }

    @Test
    fun `getFormattedAmount should format negative decimal amount correctly`() {
        // Given
        val amount = -0.01

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        assertEquals("-₹0.01", result)
    }

    @Test
    fun `getFormattedAmount should format amount with many decimal places correctly`() {
        // Given
        val amount = 123.456789

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        assertEquals("₹123.46", result) // Should round to 2 decimal places
    }

    @Test
    fun `getFormattedAmount should format negative amount with many decimal places correctly`() {
        // Given
        val amount = -123.456789

        // When
        val result = HelperUtils.getFormattedAmount(amount, true)

        // Then
        assertEquals("-₹123.46", result) // Should round to 2 decimal places
    }

    @Test
    fun `getFormattedAmount should format PnL positive amount correctly`() {
        // Given
        val amount = 1234.56
        val isForPnl = true

        // When
        val result = HelperUtils.getFormattedAmount(amount, isForPnl)

        // Then
        assertEquals("₹1,234.56", result)
    }

    @Test
    fun `getFormattedAmount should format PnL negative amount correctly`() {
        // Given
        val amount = -1234.56
        val isForPnl = true

        // When
        val result = HelperUtils.getFormattedAmount(amount, isForPnl)

        // When
        assertEquals("-₹1,234.56", result)
    }

    @Test
    fun `getFormattedAmount should format PnL zero amount correctly`() {
        // Given
        val amount = 0.0
        val isForPnl = true

        // When
        val result = HelperUtils.getFormattedAmount(amount, isForPnl)

        // Then
        assertEquals("₹0.00", result)
    }

    @Test
    fun `getFormattedAmount should handle very small positive amount`() {
        // Given
        val amount = 0.001

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        assertEquals("₹0.00", result) // Should round to 2 decimal places
    }

    @Test
    fun `getFormattedAmount should handle very small negative amount`() {
        // Given
        val amount = -0.001

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        assertEquals("-₹0.00", result) // Should round to 2 decimal places
    }

    @Test
    fun `getFormattedAmount should format amount with trailing zeros correctly`() {
        // Given
        val amount = 100.0

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        assertEquals("₹100.00", result)
    }

    @Test
    fun `getFormattedAmount should format amount with leading zeros correctly`() {
        // Given
        val amount = 0.10

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        assertEquals("₹0.10", result)
    }

    @Test
    fun `getFormattedAmount should format very large amount correctly`() {
        // Given
        val amount = 999999999.99

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        assertEquals("₹99,99,99,999.99", result)
    }

    @Test
    fun `getFormattedAmount should format very large negative amount correctly`() {
        // Given
        val amount = -999999999.99

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        assertEquals("-₹99,99,99,999.99", result)
    }

    @Test
    fun `getFormattedAmount should use Indian locale formatting`() {
        // Given
        val amount = 100000.0

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        // Indian locale uses lakhs and crores (1,00,000 instead of 100,000)
        assertEquals("₹1,00,000.00", result)
    }

    @Test
    fun `getFormattedAmount should handle edge case of maximum double value`() {
        // Given
        val amount = Double.MAX_VALUE

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        // Should not crash and should format the number
        assert(result.isNotEmpty())
        assert(result.startsWith("₹"))
    }

    @Test
    fun `getFormattedAmount should handle edge case of minimum double value`() {
        // Given
        val amount = Double.MIN_VALUE

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        // Should not crash and should format the number
        assert(result.isNotEmpty())
        assert(result.startsWith("₹"))
    }

    @Test
    fun `getFormattedAmount should handle NaN value gracefully`() {
        // Given
        val amount = Double.NaN

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        // Should not crash and should return some formatted string
        assert(result.isNotEmpty())
    }

    @Test
    fun `getFormattedAmount should handle positive infinity gracefully`() {
        // Given
        val amount = Double.POSITIVE_INFINITY

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        // Should not crash and should return some formatted string
        assert(result.isNotEmpty())
    }

    @Test
    fun `getFormattedAmount should handle negative infinity gracefully`() {
        // Given
        val amount = Double.NEGATIVE_INFINITY

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        // Should not crash and should return some formatted string
        assert(result.isNotEmpty())
    }

    @Test
    fun `getFormattedAmount should format amount with exact two decimal places`() {
        // Given
        val amount = 123.45

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        assertEquals("₹123.45", result)
    }

    @Test
    fun `getFormattedAmount should format amount with one decimal place correctly`() {
        // Given
        val amount = 123.4

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        assertEquals("₹123.40", result)
    }

    @Test
    fun `getFormattedAmount should format amount with no decimal places correctly`() {
        // Given
        val amount = 123.0

        // When
        val result = HelperUtils.getFormattedAmount(amount)

        // Then
        assertEquals("₹123.00", result)
    }
}