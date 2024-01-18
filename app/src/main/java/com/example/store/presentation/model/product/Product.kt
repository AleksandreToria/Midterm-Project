package com.example.store.presentation.model.product

data class Product(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val rating: Rate,
    val title: String
)