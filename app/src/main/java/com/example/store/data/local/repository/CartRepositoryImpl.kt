package com.example.store.data.local.repository

import com.example.store.data.local.dao.CartDao
import com.example.store.domain.local.model.CartEntity
import com.example.store.domain.local.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {
    override suspend fun getAllItems(): List<CartEntity> {
        return cartDao.getProducts()
    }

    override suspend fun addItem(cartEntity: CartEntity) {
        return cartDao.addItem(cartEntity)
    }

    override suspend fun removeItem(cartEntity: CartEntity) {
        return cartDao.removeItem(cartEntity)
    }

    override suspend fun removeAllItems() {
        cartDao.removeAllItems()
    }
}