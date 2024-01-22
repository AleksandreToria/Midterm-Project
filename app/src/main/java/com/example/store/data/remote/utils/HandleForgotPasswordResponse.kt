package com.example.store.data.remote.utils

import com.example.store.data.common.Resource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HandleForgotPasswordResponse @Inject constructor() {
    suspend fun <T : Any> apiCall(apiCall: suspend () -> T): Flow<Resource<T>> = flow {
        try {
            val response = apiCall()
            emit(Resource.Success(response))
        } catch (e: FirebaseAuthEmailException) {
            emit(Resource.Error(e.message!!))
        } catch (e: FirebaseAuthInvalidUserException) {
            emit(Resource.Error(e.message!!))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            emit(Resource.Error(e.message!!))
        } catch (e: FirebaseAuthUserCollisionException) {
            emit(Resource.Error(e.message!!))
        } catch (e: FirebaseAuthActionCodeException) {
            emit(Resource.Error(e.message!!))
        } catch (e: FirebaseException) {
            emit(Resource.Error(e.message!!))
        }
    }
}