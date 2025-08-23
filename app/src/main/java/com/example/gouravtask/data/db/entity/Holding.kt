package com.example.gouravtask.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gouravtask.presentation.model.UiHolding

@Entity(tableName = "holdings")
data class Holding(
    @PrimaryKey
    val symbol: String,
    val quantity: Int,
    val ltp: Double,
    val avgPrice: Double,
    val close: Double
)