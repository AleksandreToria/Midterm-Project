package com.example.store.domain.usecase.product

import com.example.store.data.common.Resource
import com.example.store.domain.model.store.GetProducts
import com.example.store.domain.repository.store.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductInfoUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(id: Int): Flow<Resource<GetProducts>> =
        productRepository.getProductInfo(id)
}