package com.example.store.data.repository.category

import com.example.store.data.common.Resource
import com.example.store.data.common.store.HandleResponse
import com.example.store.data.service.CategoryService
import com.example.store.domain.repository.category.CategoryRepository
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
