package com.example.gouravtask.data.api

import com.example.gouravtask.presentation.model.HoldingsResponse
import retrofit2.http.GET

interface ApiService {
    @GET("/")
    suspend fun getHoldings(): HoldingsResponse
}