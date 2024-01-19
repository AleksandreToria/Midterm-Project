package com.example.store.presentation.mapper.category

import com.example.store.domain.model.store.GetCategories
import com.example.store.presentation.model.category.Categories

fun GetCategories.toPresenter() =
    Categories(
        category = category,
    )