package com.example.gouravtask.domain.interfaces

import com.example.gouravtask.data.db.entity.Holding
import com.example.gouravtask.presentation.model.HoldingsResponseState
import com.example.gouravtask.presentation.model.UiHolding
import kotlinx.coroutines.flow.Flow

interface HoldingsRepository {
    fun getHoldings(): Flow<HoldingsResponseState>
}