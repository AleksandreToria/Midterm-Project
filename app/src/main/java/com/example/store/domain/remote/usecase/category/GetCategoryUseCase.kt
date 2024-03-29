package com.example.store.domain.remote.usecase.category

import com.example.store.data.common.Resource
import com.example.store.domain.remote.repository.category.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<String>>> =
        categoryRepository.getCategories()
}