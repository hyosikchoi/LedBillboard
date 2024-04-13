package com.hyosik.data.repository

import com.hyosik.data.local.BillboardDataSource
import com.hyosik.domain.repository.BillboardRepository
import com.hyosik.model.Billboard
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BillboardRepository @Inject constructor(
    private val billboardDataSource: BillboardDataSource
) : BillboardRepository {
    override fun getBillboard(key: String): Flow<Billboard> = billboardDataSource.getBillboard(key = key)

    override suspend fun postBillboard(billboard: Billboard) {
        billboardDataSource.editBillboard(billboard = billboard)
    }
}