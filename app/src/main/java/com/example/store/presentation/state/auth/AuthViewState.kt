package com.example.store.presentation.state.auth

import com.google.firebase.auth.FirebaseUser

data class AuthViewState(
    val isLoading: Boolean = false,
    val firebaseUser: FirebaseUser? = null,
    val errorMessage: String? = null
)