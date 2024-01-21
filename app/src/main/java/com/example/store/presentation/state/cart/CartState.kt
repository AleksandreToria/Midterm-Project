package com.example.store.presentation.state.cart

import com.example.store.domain.local.model.CartEntity
import com.example.store.presentation.model.product.Product

data class CartState(
    val products: List<Product>? = null,
    val errorMessage: String? = null,
    val allItems: List<CartEntity>? = emptyList(),
)