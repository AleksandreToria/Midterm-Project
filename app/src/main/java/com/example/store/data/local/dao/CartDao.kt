package com.example.store.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.store.domain.local.model.CartEntity

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items ORDER BY id ASC")
    suspend fun getProducts(): List<CartEntity>

    @Query("DELETE FROM cart_items")
    suspend fun removeAllItems()

    @Delete
    suspend fun removeItem(cartEntity: CartEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(cartEntity: CartEntity)
}