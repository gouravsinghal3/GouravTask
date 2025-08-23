package com.example.gouravtask

import com.example.gouravtask.data.di.AppModule
import com.example.gouravtask.domain.usecase.CalculatePortfolioSummaryUseCase
import com.example.gouravtask.domain.usecase.GetHoldingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestModule {

    @Provides
    @Singleton
    fun provideGetHoldingsUseCase(): GetHoldingsUseCase = mockk(relaxed = true)

    @Provides
    @Singleton
    fun provideCalculatePortfolioSummaryUseCase(): CalculatePortfolioSummaryUseCase = mockk(relaxed = true)
}