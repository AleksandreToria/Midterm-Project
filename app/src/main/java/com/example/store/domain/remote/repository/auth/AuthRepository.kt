package com.example.store.domain.remote.repository.auth

import com.example.store.data.common.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun logIn(email: String, password: String): Flow<Resource<FirebaseUser>>
    suspend fun register(email: String, password: String): Flow<Resource<FirebaseUser>>
}