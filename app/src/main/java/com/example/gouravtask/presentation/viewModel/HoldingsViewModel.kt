package com.example.gouravtask.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gouravtask.data.db.entity.Holding
import com.example.gouravtask.domain.usecase.CalculatePortfolioSummaryUseCase
import com.example.gouravtask.domain.usecase.GetHoldingsUseCase
import com.example.gouravtask.presentation.model.HoldingsUiState
import com.example.gouravtask.presentation.model.PortfolioSummary
import com.example.gouravtask.presentation.model.UiHolding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoldingsViewModel @Inject constructor(
    private val getHoldingsUseCase: GetHoldingsUseCase,
    private val calculatePortfolioSummaryUseCase: CalculatePortfolioSummaryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HoldingsUiState())
    val uiState: StateFlow<HoldingsUiState> = _uiState.asStateFlow()

// Cache calculated values
    private var cachedHoldings = emptyList<UiHolding>()
    private var cachedSummary: PortfolioSummary? = null
    init {
        fetchHoldings()
    }

    fun fetchHoldings() {
        viewModelScope.launch {
            getHoldingsUseCase()
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(error = "Failed to fetch holdings: ${e.message}", isLoading = false)
                    }
                }
                .collect { holdings ->
                    // Only recalculate if data changed
                    if (holdings != cachedHoldings) {
                        cachedHoldings = holdings
                        cachedSummary = calculatePortfolioSummaryUseCase(holdings)

                        _uiState.update {
                            it.copy(
                                holdings = holdings,
                                portfolioSummary = cachedSummary,
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }
}