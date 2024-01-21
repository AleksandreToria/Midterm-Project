package com.example.store.di

import com.example.store.data.remote.repository.auth.AuthRepositoryImpl
import com.example.store.data.remote.repository.category.CategoryRepositoryImpl
import com.example.store.data.remote.repository.store.ProductRepositoryImpl
import com.example.store.domain.remote.repository.auth.AuthRepository
import com.example.store.domain.remote.repository.category.CategoryRepository
import com.example.store.domain.remote.repository.store.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideAuthRepository(repositoryImpl: AuthRepositoryImpl): AuthRepository {
        return repositoryImpl
    }

    @Singleton
    @Provides
    fun provideProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository {
        return productRepositoryImpl
    }

    @Singleton
    @Provides
    fun provideCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository {
        return categoryRepositoryImpl
    }
}
