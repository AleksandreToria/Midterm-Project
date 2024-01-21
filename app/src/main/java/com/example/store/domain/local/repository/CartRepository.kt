package com.example.store.domain.local.repository

import com.example.store.domain.local.model.CartEntity

interface CartRepository {
    suspend fun getAllItems(): List<CartEntity>
    suspend fun addItem(cartEntity: CartEntity)
    suspend fun removeItem(cartEntity: CartEntity)
    suspend fun removeAllItems()
}