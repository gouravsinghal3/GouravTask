package com.example.gouravtask.utils

import java.util.Locale

object HelperUtils {

    fun getFormattedAmount(amount: Double, isForPnl: Boolean = false): String {
        if (amount.isNaN() || amount.isInfinite()) {
            return "₹--"
        }
        if (amount == 0.0) {
            return "₹0.00"
        }
        val sign = if ((amount < 0 && isForPnl) || (amount < 0 && !isForPnl)) "-" else ""
        val formatted = formatIndianAmount(amount)
        return sign + "₹" + formatted
    }

    fun formatIndianAmount(value: Double): String {
        val absValue = kotlin.math.abs(value)
        val roundedStr = String.format(Locale.US,"%.2f", absValue)
        val parts = roundedStr.split(".")
        val intPart = parts[0]
        val decPart = parts[1]
        val sb = StringBuilder()
        val len = intPart.length
        if (len > 3) {
            val firstGroup = intPart.substring(0, len - 3)
            // Add commas after every 2 digits for the firstGroup
            var i = firstGroup.length % 2
            if (i == 0) i = 2
            sb.append(firstGroup.substring(0, i))
            var j = i
            while (j < firstGroup.length) {
                sb.append(",").append(firstGroup.substring(j, j + 2))
                j += 2
            }
            sb.append(",")
            sb.append(intPart.substring(len - 3))
        } else {
            sb.append(intPart)
        }
        sb.append(".").append(decPart)
        return sb.toString()
    }

}