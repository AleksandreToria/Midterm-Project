package com.example.store.data.repository.store

import com.example.store.data.common.Resource
import com.example.store.data.common.store.HandleStoreResponse
import com.example.store.data.common.store.mapProductInfoToDomain
import com.example.store.data.common.store.mapProductToDomain
import com.example.store.data.mapper.toDomain
import com.example.store.data.service.ProductService
import com.example.store.domain.model.store.GetProducts
import com.example.store.domain.repository.store.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val handleStoreResponse: HandleStoreResponse,
    private val productService: ProductService
) : ProductRepository {
    override suspend fun getProducts(): Flow<Resource<List<GetProducts>>> {
        return handleStoreResponse.apiCall {
            productService.getProducts()
        }.mapProductToDomain {
            it.toDomain()
        }
    }

    override suspend fun getProductInfo(id: Int): Flow<Resource<GetProducts>> {
        return handleStoreResponse.apiCall {
            productService.getUserInfo(id)
        }.mapProductInfoToDomain {
            it.toDomain()
        }
    }
}