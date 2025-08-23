package com.example.gouravtask.data.repo

import android.util.Log
import com.example.gouravtask.domain.interfaces.HoldingsRepository
import com.example.gouravtask.data.api.ApiService
import com.example.gouravtask.data.db.HoldingsDao
import com.example.gouravtask.data.db.entity.Holding
import com.example.gouravtask.presentation.model.UiHolding
import com.example.gouravtask.presentation.model.toHolding
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

    override fun getHoldings(): Flow<List<UiHolding>> = flow {
        Log.d("HoldingsRepositoryImpl", "Starting getHoldings()")

        // Get current cached data first (non-blocking)
        val currentCachedHoldings = holdingsDao.getAllHoldings().first()
        Log.d(
            "HoldingsRepositoryImpl",
            "Current cached holdings count: ${currentCachedHoldings.size}"
        )

        // Emit current cached data immediately
        emit(currentCachedHoldings.map { it.toUiHolding() })

        // Then, try to fetch from network
        try {
            Log.d("HoldingsRepositoryImpl", "Making API call to fetch holdings...")
            val networkHoldings = apiService.getHoldings()
            Log.d(
                "HoldingsRepositoryImpl",
                "API call successful, received ${networkHoldings.data.userHolding.size} holdings"
            )

            // Clear old data and insert new data
            holdingsDao.clearAll()
            holdingsDao.insertAll(networkHoldings.data.userHolding.map { it.toHolding() })
            Log.d("HoldingsRepositoryImpl", "Successfully updated database with new holdings")

        } catch (e: Exception) {
            Log.e("HoldingsRepositoryImpl", "Error fetching holdings from API: ${e.message}", e)
            Log.e("HoldingsRepositoryImpl", "Exception type: ${e.javaClass.simpleName}")

            // Log more details about the exception
            when (e) {
                is UnknownHostException -> Log.e(
                    "HoldingsRepositoryImpl",
                    "Network error: No internet connection or host not found"
                )

                is SocketTimeoutException -> Log.e(
                    "HoldingsRepositoryImpl",
                    "Network error: Request timeout"
                )

                is HttpException -> Log.e(
                    "HoldingsRepositoryImpl",
                    "HTTP error: ${e.code()} - ${e.message()}"
                )

                else -> Log.e("HoldingsRepositoryImpl", "Other error: ${e.message}")
            }

            // Network error, we'll continue with cached data
        }

        // Now emit the database flow for future updates
        Log.d("HoldingsRepositoryImpl", "Emitting database flow for future updates")
        emitAll(holdingsDao.getAllHoldings().map { holdingList -> 
            holdingList.map { it.toUiHolding() } 
        })

    }.flowOn(Dispatchers.IO)
}