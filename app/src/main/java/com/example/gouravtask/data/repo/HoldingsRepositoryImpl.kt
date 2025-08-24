package com.example.gouravtask.data.repo

import android.util.Log
import com.example.gouravtask.domain.interfaces.HoldingsRepository
import com.example.gouravtask.data.api.ApiService
import com.example.gouravtask.data.db.HoldingsDao
import com.example.gouravtask.data.db.entity.Holding
import com.example.gouravtask.data.db.entity.toHolding
import com.example.gouravtask.presentation.model.HoldingsResponseState
import com.example.gouravtask.presentation.model.UiHolding
import com.example.gouravtask.presentation.model.toUiHolding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class HoldingsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val holdingsDao: HoldingsDao
) : HoldingsRepository {

    override fun getHoldings(): Flow<HoldingsResponseState> = flow {
        // Get current cached data first (non-blocking)
        val currentCachedHoldings = holdingsDao.getAllHoldings().first()

        // Emit current cached data immediately
        emit(HoldingsResponseState.Success(currentCachedHoldings.map { it.toUiHolding() }))

        // Then, try to fetch from network
        try {
            val networkHoldings = apiService.getHoldings()

            // Clear old data and insert new data
            holdingsDao.clearAll()
            holdingsDao.insertAll(networkHoldings.data.userHolding.map { it.toHolding() })

        } catch (e: Exception) {
            when (e) {
                is UnknownHostException ->  emit(HoldingsResponseState.Error("Network error: No internet connection or host not found"))

                is SocketTimeoutException -> emit(HoldingsResponseState.Error("Network error: Request timeout"))

                else -> {
                    emit(HoldingsResponseState.Error(e.message ?: "Something went wrong. Try after sometime."))
                }
            }

            return@flow
        }

        // Now emit the database flow for future updates
        emitAll(holdingsDao.getAllHoldings().map { holdingList ->
            HoldingsResponseState.Success(
                holdingList.map { it.toUiHolding() }
            )
        })
    }.flowOn(Dispatchers.IO)

}
