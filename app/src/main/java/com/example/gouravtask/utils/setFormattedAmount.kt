package com.example.gouravtask.utils

import android.content.Context
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.example.gouravtask.R
import java.text.NumberFormat
import java.util.Locale

/**
 * Extension function to format amount with currency symbol and color coding
 * @param amount The amount to format
 * @param isNegative Whether to treat the amount as negative (for P&L values)
 * @param showSign Whether to show + sign for positive values
 */



@StyleRes
fun Context.themeStyleRes(@AttrRes attrRes: Int): Int = TypedValue()
    .apply { theme.resolveAttribute (attrRes, this, true) }
    .resourceId
/**
 * Extension function to get dimension value in sp units (alternative method)
 */
fun Context.getDimensionSpAlternative(dimenRes: Int): Int {
    return resources.getDimensionPixelSize(dimenRes)
}

fun Context.getDimensionSp(dimenRes: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        dimenRes,
        resources.displayMetrics
    ).toInt()
}

fun TextView.setFormattedAmount(amount: Double, isForPnl: Boolean = false) {
    this.text = HelperUtils.getFormattedAmount(amount = amount, isForPnl = isForPnl)
    
    // Set text color based on amount and isNegative flag
    var textColor =  ContextCompat.getColor(context, R.color.black)
    if(isForPnl) {
         textColor = when {
            amount > 0 -> ContextCompat.getColor(context, R.color.green_500)
            amount < 0 -> ContextCompat.getColor(context, R.color.red_500)
            else -> ContextCompat.getColor(context, R.color.black)
        }
    }
    
    this.setTextColor(textColor)
}

/**
 * Extension function to create SpannableString with different sizes and colors
 * @param firstPart The first part of the text
 * @param firstPartSize The text size for the first part in sp
 * @param firstPartColor The color for the first part
 * @param secondPart The second part of the text
 * @param secondPartSize The text size for the second part in sp
 * @param secondPartColor The color for the second part
 */
fun TextView.setSpannableText(
    firstPart: String,
    firstPartSize: Int,
    firstPartColor: Int,
    secondPart: String,
    secondPartSize: Int,
    secondPartColor: Int
) {
    val spannableString = SpannableString("$firstPart $secondPart")

    // Apply styling to first part
    spannableString.setSpan(
        AbsoluteSizeSpan(firstPartSize, true), // true for sp units
        0,
        firstPart.length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    spannableString.setSpan(
        ForegroundColorSpan(firstPartColor),
        0,
        firstPart.length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Apply styling to second part
    spannableString.setSpan(
        AbsoluteSizeSpan(secondPartSize, true), // true for sp units
        firstPart.length,
        firstPart.length + secondPart.length +1,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    spannableString.setSpan(
        ForegroundColorSpan(secondPartColor),
        firstPart.length,
        firstPart.length + secondPart.length + 1,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    this.text = spannableString
}

/**
 * Extension function to create SpannableString with different sizes and colors using resource colors
 * @param firstPart The first part of the text
 * @param firstPartSize The text size for the first part in sp
 * @param firstPartColorRes The color resource for the first part
 * @param secondPart The second part of the text
 * @param secondPartSize The text size for the second part in sp
 * @param secondPartColorRes The color resource for the second part
 */
fun TextView.setSpannableTextWithResources(
    firstPart: Int,
    firstPartTxtSize: Float,
    firstPartColorRes: Int,
    amount: Number,
    secondPartColorRes: Int,
    secondPartTxtSize: Float,
    isFormattedAmount: Boolean = false,
    isForPnl: Boolean = false
) {
   val labelTextSize = context.getDimensionSp(firstPartTxtSize)
    val secondTextSize = context.getDimensionSp(secondPartTxtSize)
    val label = ContextCompat.getString(context, firstPart)
    val firstColor = ContextCompat.getColor(context, firstPartColorRes)
    val secondColor = ContextCompat.getColor(context, secondPartColorRes)
    var secondPart = amount.toString()
    if(isFormattedAmount){
        secondPart = HelperUtils.getFormattedAmount(amount.toDouble(), isForPnl)
    }
    setSpannableText(label, labelTextSize, firstColor, secondPart, secondTextSize, secondColor)
}
