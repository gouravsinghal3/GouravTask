package com.example.gouravtask.data.db.entity

import com.example.gouravtask.presentation.model.UiHolding

data class NetworkHolding(
    val symbol: String,
    val quantity: Int,
    val ltp: Double,
    val avgPrice: Double,
    val close: Double,
)


fun NetworkHolding.toHolding(): Holding {
    return Holding(
        symbol = this.symbol,
        quantity = this.quantity,
        ltp = this.ltp,
        avgPrice = this.avgPrice,
        close = this.close
    )
}
