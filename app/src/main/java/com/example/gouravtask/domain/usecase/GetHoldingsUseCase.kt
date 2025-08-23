package com.example.gouravtask.domain.usecase

import android.util.Log
import com.example.gouravtask.domain.interfaces.HoldingsRepository
import javax.inject.Inject

class GetHoldingsUseCase @Inject constructor(
    private val repository: HoldingsRepository
) {
    operator fun invoke() = run {
        Log.d("GetHoldingsUseCase", "UseCase invoked, calling repository.getHoldings()")
        repository.getHoldings()
    }
}
