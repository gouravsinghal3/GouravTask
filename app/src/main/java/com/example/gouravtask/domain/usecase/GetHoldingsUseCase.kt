package com.example.gouravtask.domain.usecase

import com.example.gouravtask.domain.interfaces.HoldingsRepository
import javax.inject.Inject

class GetHoldingsUseCase @Inject constructor(
    private val repository: HoldingsRepository
) {
    operator fun invoke() = run {
        repository.getHoldings()
    }
}
