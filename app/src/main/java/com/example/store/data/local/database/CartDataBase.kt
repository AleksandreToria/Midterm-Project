package com.example.store.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.store.data.local.dao.CartDao
import com.example.store.domain.local.model.CartEntity

@Database(entities = [CartEntity::class], version = 1)
abstract class CartDataBase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}