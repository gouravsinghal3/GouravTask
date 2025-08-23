package com.example.gouravtask.utils

import java.text.NumberFormat
import java.util.Locale

object HelperUtils {

    fun getFormattedAmount(amount: Double, isForPnl: Boolean = false): String {

        val indiaLocale = Locale.Builder()
            .setLanguage("en")
            .setRegion("IN")
            .build()

        val currencyFormatter = NumberFormat.getCurrencyInstance(indiaLocale)
        val formattedAmount = when {
            amount == 0.0 -> currencyFormatter.format(0.0)
            amount < 0 -> {
                val prefix = if (isForPnl) "-" else ""
                prefix + currencyFormatter.format(kotlin.math.abs(amount))
            }

            else -> currencyFormatter.format(amount)
        }
        return formattedAmount
    }
}