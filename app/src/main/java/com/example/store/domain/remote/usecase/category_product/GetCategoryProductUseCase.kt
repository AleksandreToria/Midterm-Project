package com.example.store.domain.remote.usecase.category_product

import com.example.store.data.common.Resource
import com.example.store.domain.remote.model.store.GetProducts
import com.example.store.domain.remote.repository.store.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(category: String): Flow<Resource<List<GetProducts>>> =
        productRepository.getCategoryProducts(category)
}