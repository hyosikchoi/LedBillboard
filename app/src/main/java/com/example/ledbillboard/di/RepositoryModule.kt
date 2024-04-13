package com.example.ledbillboard.di

import com.hyosik.data.repository.BillboardRepositoryImpl
import com.hyosik.domain.repository.BillboardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideBillboardRepository(
        repository: BillboardRepositoryImpl
    ): BillboardRepository

}