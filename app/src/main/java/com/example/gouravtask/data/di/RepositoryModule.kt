package com.example.gouravtask.data.di

import com.example.gouravtask.domain.interfaces.HoldingsRepository
import com.example.gouravtask.data.repo.HoldingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHoldingsRepository(
        holdingsRepositoryImpl: HoldingsRepositoryImpl
    ): HoldingsRepository
}