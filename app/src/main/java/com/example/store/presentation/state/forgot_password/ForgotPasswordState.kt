package com.example.store.presentation.state.forgot_password

import com.example.store.data.common.Resource

data class ForgotPasswordState(
    val resource: Resource<Unit>?,
    val errorMessage: String? = null
)