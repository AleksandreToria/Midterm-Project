package com.example.store.presentation.state.product

import com.example.store.presentation.model.product.Product

data class ProductState(
    val isLoading: Boolean = false,
    val products: List<Product>? = null,
    val categories: List<String>? = null,
    val categoryProducts: List<Product>? = null,
    val searchedProducts: List<Product>? = null,
    val productInfo: Product? = null,
    val errorMessage: String? = null,
)
