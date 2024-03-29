package com.example.store.domain.remote.usecase.log_in

import com.example.store.data.common.Resource
import com.example.store.domain.remote.repository.auth.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogInUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Flow<Resource<FirebaseUser>> =
        authRepository.logIn(email, password)
}
