package com.example.store.presentation.model.product

import com.example.store.domain.model.store.Rating

data class Product(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val rating: Rating,
    val title: String
)