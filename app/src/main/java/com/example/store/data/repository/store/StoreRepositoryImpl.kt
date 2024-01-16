package com.example.store.data.repository.store

import com.example.store.data.common.Resource
import com.example.store.data.common.store.HandleStoreResponse
import com.example.store.data.common.store.mapStoreToDomain
import com.example.store.data.mapper.toDomain
import com.example.store.data.service.ProductService
import com.example.store.domain.model.store.GetProducts
import com.example.store.domain.repository.store.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val handleStoreResponse: HandleStoreResponse,
    private val productService: ProductService
) : ProductRepository {
    override suspend fun getProducts(): Flow<Resource<List<GetProducts>>> {
        return handleStoreResponse.apiCall {
            productService.getProducts()
        }.mapStoreToDomain { storeEntities ->
            storeEntities.toDomain()
        }
    }
}