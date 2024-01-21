package com.example.store.domain.remote.repository.store

import com.example.store.data.common.Resource
import com.example.store.domain.remote.model.store.GetProducts
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(): Flow<Resource<List<GetProducts>>>
    suspend fun getCategoryProducts(category: String): Flow<Resource<List<GetProducts>>>
    suspend fun getProductInfo(id: Int): Flow<Resource<GetProducts>>
}