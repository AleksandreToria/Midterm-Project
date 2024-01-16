package com.example.store.domain.usecase.registration

import com.example.store.data.common.Resource
import com.example.store.domain.repository.auth.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Flow<Resource<FirebaseUser>> =
        authRepository.register(email, password)
}