package com.example.store.domain.remote.usecase.forgot_password

import com.example.store.data.common.Resource
import com.example.store.domain.remote.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Flow<Resource<Unit>> {
        return authRepository.forgotPassword(email)
    }
}