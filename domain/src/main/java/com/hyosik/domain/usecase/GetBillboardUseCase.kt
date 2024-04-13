package com.hyosik.domain.usecase

import com.hyosik.domain.repository.BillboardRepository
import com.hyosik.model.Billboard
import kotlinx.coroutines.flow.Flow

class GetBillboardUseCase(
    private val billboardRepository: BillboardRepository
) {
    operator fun invoke(key: String): Flow<Billboard> = billboardRepository.getBillboard(key = key)
}