package com.example.store.data.repository.auth

import com.example.store.data.common.Resource
import com.example.store.data.common.firebase.HandleAuthResponse
import com.example.store.domain.repository.auth.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val handleAuthResponse: HandleAuthResponse
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

    override suspend fun googleSignIn(idToken: String): Flow<Resource<FirebaseUser>> {
        return handleAuthResponse.apiCall {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
        }
    }
}
