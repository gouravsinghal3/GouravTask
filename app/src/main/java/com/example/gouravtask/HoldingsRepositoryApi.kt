package com.example.gouravtask

import com.example.gouravtask.data.api.ApiService
import com.example.gouravtask.presentation.model.HoldingsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HoldingsRepositoryApi {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://35dee773a9ec441e9f38d5fc249406ce.api.mockbin.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    suspend fun getHoldings(): HoldingsResponse {
        return apiService.getHoldings()
    }
}
