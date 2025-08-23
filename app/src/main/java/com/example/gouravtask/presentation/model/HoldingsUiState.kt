package com.example.gouravtask.presentation.model

import com.example.gouravtask.data.db.entity.Holding

data class HoldingsUiState(
    val holdings: List<UiHolding> = emptyList(),
    val portfolioSummary: PortfolioSummary? = null,
    val isSummaryExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)