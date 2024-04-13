package com.hyosik.data.local

import com.hyosik.model.Billboard
import kotlinx.coroutines.flow.Flow

interface BillboardDataSource {

    fun getBillboard(key: String): Flow<Billboard>

    suspend fun editBillboard(billboard: Billboard)
}