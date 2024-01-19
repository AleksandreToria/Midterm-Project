package com.example.store.domain.usecase.category_product

import com.example.store.data.common.Resource
import com.example.store.domain.model.store.GetProducts
import com.example.store.domain.repository.store.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(category: String): Flow<Resource<List<GetProducts>>> =
        productRepository.getCategoryProducts(category)
}