package com.example.store.domain.usecase.product

import com.example.store.data.common.Resource
import com.example.store.domain.model.store.GetProducts
import com.example.store.domain.repository.store.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<GetProducts>>> =
        productRepository.getProducts()
}