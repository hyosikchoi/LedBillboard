package com.hyosik.domain.repository

import com.hyosik.model.Billboard
import kotlinx.coroutines.flow.Flow

interface BillboardRepository {

    fun getBillboard(key: String): Flow<Billboard>

    suspend fun postBillboard(billboard: Billboard)

}