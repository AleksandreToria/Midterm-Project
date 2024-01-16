package com.example.store.domain.usecase.log_in

import com.example.store.data.common.Resource
import com.example.store.domain.repository.auth.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(idToken: String): Flow<Resource<FirebaseUser>> =
        authRepository.googleSignIn(idToken)
}