package com.hyosik.domain.usecase

import com.hyosik.domain.repository.BillboardRepository
import com.hyosik.model.Billboard

class PostBillboardUseCase(
    private val billboardRepository: BillboardRepository
) {
    suspend operator fun invoke(billboard: Billboard) {
       billboardRepository.postBillboard(billboard = billboard)
    }
}