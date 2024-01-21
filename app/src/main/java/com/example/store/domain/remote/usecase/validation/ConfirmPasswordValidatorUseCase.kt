package com.example.store.domain.remote.usecase.validation

import javax.inject.Inject

class ConfirmPasswordValidatorUseCase @Inject constructor() {
    operator fun invoke(passwordOne: String, passwordTwo: String): Boolean {
        return passwordOne == passwordTwo
    }
}