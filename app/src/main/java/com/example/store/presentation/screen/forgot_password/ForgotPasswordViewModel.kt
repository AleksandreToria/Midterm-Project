package com.example.store.presentation.screen.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.common.Resource
import com.example.store.domain.remote.usecase.forgot_password.ForgotPasswordUseCase
import com.example.store.domain.remote.usecase.validation.EmailValidatorUseCase
import com.example.store.presentation.event.forgot_password.ForgotPasswordEvents
import com.example.store.presentation.screen.log_in.LogInViewModel
import com.example.store.presentation.state.forgot_password.ForgotPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val emailValidator: EmailValidatorUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase
): ViewModel() {
    private val _forgotPasswordState = MutableStateFlow(ForgotPasswordState(null))
    val forgotPasswordState get() = _forgotPasswordState

    private val _uiEvent = MutableSharedFlow<ForgotPasswordUiEvent>()
    val uiEvent: SharedFlow<ForgotPasswordUiEvent> get() = _uiEvent

    fun onEvent(event: ForgotPasswordEvents) {
        when (event) {
            is ForgotPasswordEvents.SendEmail -> validateForm(event.email)
            is ForgotPasswordEvents.ResetErrorMessage -> updateErrorMessage(null)
        }
    }

    private fun forgotPassword(email: String) =
        viewModelScope.launch {
        forgotPasswordUseCase(email).collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _forgotPasswordState.update {
                        it.copy(
                            resource = resource,
                            errorMessage = null
                        )
                    }
                }

                is Resource.Error -> {
                    _forgotPasswordState.update {
                        it.copy(
                            resource = null,
                            errorMessage = resource.errorMessage
                        )
                    }
                }

                else -> {}
            }
        }
    }

    private fun validateForm(email: String) {
        val isEmailValid = emailValidator(email)

        val isFieldValid = listOf(isEmailValid).all { it }

        if (!isFieldValid) {
            updateErrorMessage(message = "Fields are not valid!")
            return
        }

        forgotPassword(email = email)
    }

    fun navigateToLogin() {
        viewModelScope.launch {
            _uiEvent.emit(ForgotPasswordUiEvent.NavigateToLogin)
        }
    }

    private fun updateErrorMessage(message: String?) {
        _forgotPasswordState.update { currentState -> currentState.copy(errorMessage = message) }
    }

    sealed interface ForgotPasswordUiEvent {
        data object NavigateToLogin : ForgotPasswordUiEvent
    }
}