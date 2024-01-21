package com.example.store.di

import android.content.Context
import androidx.room.Room
import com.example.store.data.local.dao.CartDao
import com.example.store.data.local.database.CartDataBase
import com.example.store.data.local.repository.CartRepositoryImpl
import com.example.store.domain.local.repository.CartRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): CartDataBase =
        Room.databaseBuilder(
            context, CartDataBase::class.java, "RECIPE_DATABASE"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideDao(db: CartDataBase) = db.cartDao()

    @Provides
    fun provideFavouriteRecipeRepository(cartDao: CartDao): CartRepository {
        return CartRepositoryImpl(cartDao)
    }
}