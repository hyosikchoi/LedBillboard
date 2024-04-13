package com.example.ledbillboard.di

import com.hyosik.domain.repository.BillboardRepository
import com.hyosik.domain.usecase.GetBillboardUseCase
import com.hyosik.domain.usecase.PostBillboardUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetBillboardUseCase(
        billboardRepository: BillboardRepository
    ): GetBillboardUseCase = GetBillboardUseCase(billboardRepository = billboardRepository)

    @Provides
    fun providePostBillboardUseCase(
        billboardRepository: BillboardRepository
    ): PostBillboardUseCase = PostBillboardUseCase(billboardRepository = billboardRepository)

}