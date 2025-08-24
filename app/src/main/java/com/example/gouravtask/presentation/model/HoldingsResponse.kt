package com.example.gouravtask.presentation.model

import androidx.room.PrimaryKey
import com.example.gouravtask.data.db.entity.Holding
import com.example.gouravtask.data.db.entity.NetworkHolding
import com.google.gson.annotations.SerializedName

// This class maps directly to the JSON structure from the API


sealed class HoldingsResponseState {
       data class Success(val holdings: List<UiHolding>) : HoldingsResponseState()
       data class Error(val message: String) : HoldingsResponseState()
}
data class HoldingsResponse(
    @SerializedName("data")
    val data: Data
)

data class Data(
    @SerializedName("userHolding")
    val userHolding: List<NetworkHolding>
)

data class UiHolding(
    val symbol: String = "",
    val quantity: Int = 0,
    val ltp: Double = 0.0,
    val avgPrice: Double = 0.0,
    val close: Double = 0.0,
    val error: String? = null
)

fun Holding.toUiHolding(): UiHolding {
    return UiHolding(
        symbol = this.symbol,
        quantity = this.quantity,
        ltp = this.ltp,
        avgPrice = this.avgPrice,
        close = this.close,
        error = null
    )
}

data class PortfolioSummary(
    val currentValue: Double,
    val totalInvestment: Double,
    val todaysPnl: Double,
    val totalPnl: Double
)
