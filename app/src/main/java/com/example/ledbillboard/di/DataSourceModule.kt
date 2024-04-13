package com.example.ledbillboard.di

import com.hyosik.data.local.BillboardDataSource
import com.hyosik.data.local.BillboardDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun provideBillboardDataSource(
        dataSource: BillboardDataSourceImpl
    ): BillboardDataSource
}