package com.example.store.data.remote.repository.category

import com.example.store.data.common.HandleResponse
import com.example.store.data.common.Resource
import com.example.store.data.remote.service.CategoryService
import com.example.store.domain.remote.repository.category.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryService: CategoryService,
    private val handleResponse: HandleResponse
) : CategoryRepository {
    override suspend fun getCategories(): Flow<Resource<List<String>>> {
        return handleResponse.apiCall {
            categoryService.getCategories()
        }
    }
}
