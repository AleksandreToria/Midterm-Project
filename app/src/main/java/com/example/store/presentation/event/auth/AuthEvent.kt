package com.example.store.presentation.event.auth

sealed class AuthEvent {
    data class LogIn(val email: String, val password: String) : AuthEvent()
    data class Register(val email: String, val password: String, val confirmPassword: String) :
        AuthEvent()

    data object ResetErrorMessage : AuthEvent()
}