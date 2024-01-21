package com.example.store.domain.remote.repository.category

import com.example.store.data.common.Resource
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getCategories(): Flow<Resource<List<String>>>
}