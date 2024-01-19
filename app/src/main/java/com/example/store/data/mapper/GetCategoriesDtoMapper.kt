package com.example.store.data.mapper

import com.example.store.data.model.store.GetCategoriesDto
import com.example.store.domain.model.store.GetCategories

fun GetCategoriesDto.toDomain(): GetCategories {
    return GetCategories(category)
}