package com.example.store.di

import com.example.store.data.repository.auth.AuthRepositoryImpl
import com.example.store.data.repository.store.StoreRepositoryImpl
import com.example.store.domain.repository.auth.AuthRepository
import com.example.store.domain.repository.store.ProductRepository
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
    fun provideProductRepository(storeRepositoryImpl: StoreRepositoryImpl): ProductRepository {
        return storeRepositoryImpl
    }
}
