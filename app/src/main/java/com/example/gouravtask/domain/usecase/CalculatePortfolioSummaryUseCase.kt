package com.example.gouravtask.domain.usecase


import com.example.gouravtask.data.db.entity.Holding
import com.example.gouravtask.presentation.model.PortfolioSummary
import com.example.gouravtask.presentation.model.UiHolding
import javax.inject.Inject

class CalculatePortfolioSummaryUseCase @Inject constructor() {
    operator fun invoke(holdings: List<UiHolding>): PortfolioSummary {
        val currentValue = holdings.sumOf { it.ltp * it.quantity }
        val totalInvestment = holdings.sumOf { it.avgPrice * it.quantity }
        val todayPnl = holdings.sumOf { (it.close - it.ltp) * it.quantity }
        val totalPnl = currentValue - totalInvestment

        return PortfolioSummary(currentValue, totalInvestment, todayPnl, totalPnl)
    }
}
