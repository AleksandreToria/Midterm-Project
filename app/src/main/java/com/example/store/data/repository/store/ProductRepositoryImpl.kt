package com.example.store.data.repository.store

import com.example.store.data.common.Resource
import com.example.store.data.common.store.HandleResponse
import com.example.store.data.common.store.mapProductInfoToDomain
import com.example.store.data.common.store.mapToDomain
import com.example.store.data.mapper.toDomain
import com.example.store.data.service.ProductService
import com.example.store.domain.model.store.GetProducts
import com.example.store.domain.repository.store.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val handleResponse: HandleResponse,
    private val productService: ProductService
) : ProductRepository {
    override suspend fun getProducts(): Flow<Resource<List<GetProducts>>> {
        return handleResponse.apiCall {
            productService.getProducts()
        }.mapToDomain {
            it.toDomain()
        }
    }

    override suspend fun getCategoryProducts(category: String): Flow<Resource<List<GetProducts>>> {
        return handleResponse.apiCall {
            productService.getCategoryProducts(category)
        }.mapToDomain {
            it.toDomain()
        }
    }

    override suspend fun getProductInfo(id: Int): Flow<Resource<GetProducts>> {
        return handleResponse.apiCall {
            productService.getUserInfo(id)
        }.mapProductInfoToDomain {
            it.toDomain()
        }
    }
}