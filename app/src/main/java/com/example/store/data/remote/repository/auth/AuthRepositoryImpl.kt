package com.example.store.data.remote.repository.auth

import com.example.store.data.common.Resource
import com.example.store.data.remote.utils.HandleAuthResponse
import com.example.store.data.remote.utils.HandleForgotPasswordResponse
import com.example.store.domain.remote.repository.auth.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val handleAuthResponse: HandleAuthResponse,
    private val handleForgotPasswordResponse: HandleForgotPasswordResponse
) : AuthRepository {
    override suspend fun logIn(
        email: String,
        password: String
    ): Flow<Resource<FirebaseUser>> {
        return handleAuthResponse.apiCall {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        }
    }

    override suspend fun register(
        email: String,
        password: String
    ): Flow<Resource<FirebaseUser>> {
        return handleAuthResponse.apiCall {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        }
    }

    override suspend fun forgotPassword(email: String): Flow<Resource<Unit>> {
        return handleForgotPasswordResponse.apiCall {
            firebaseAuth.sendPasswordResetEmail(email)
        }
    }
}