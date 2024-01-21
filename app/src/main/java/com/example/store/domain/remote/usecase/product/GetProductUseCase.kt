package com.example.store.domain.remote.usecase.product

import com.example.store.data.common.Resource
import com.example.store.domain.remote.model.store.GetProducts
import com.example.store.domain.remote.repository.store.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<GetProducts>>> =
        productRepository.getProducts()
}