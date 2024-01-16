package com.example.store.data.common.store

import com.example.store.data.common.Resource
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class HandleStoreResponse @Inject constructor() {
    fun <T : Any> apiCall(call: suspend () -> Response<T>) = flow {
        emit(Resource.Loading(loading = true))
        try {
            val response = call()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                emit(Resource.Success(data = body))
            } else {
                emit(Resource.Error(errorMessage = response.errorBody()?.string() ?: ""))
            }
        } catch (e: Throwable) {
            emit(Resource.Error(errorMessage = e.message ?: ""))
        }
        emit(Resource.Loading(loading = false))
    }
}
