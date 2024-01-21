package com.example.store.data.remote.utils

import com.example.store.data.common.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HandleAuthResponse @Inject constructor() {
    fun apiCall(call: suspend () -> AuthResult): Flow<Resource<FirebaseUser>> = flow {
        emit(Resource.Loading(loading = true))
        try {
            val authResult = call()
            val user = authResult.user
            if (user != null) {
                emit(Resource.Success(data = user))
            } else {
                emit(Resource.Error(errorMessage = "Login failed"))
            }
        } catch (e: Throwable) {
            emit(Resource.Error(errorMessage = e.message ?: "Unknown error"))
        }
        emit(Resource.Loading(loading = false))
    }
}
