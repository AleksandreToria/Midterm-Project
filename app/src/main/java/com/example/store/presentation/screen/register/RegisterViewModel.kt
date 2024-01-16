package com.example.store.presentation.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.common.Resource
import com.example.store.domain.usecase.registration.RegisterUseCase
import com.example.store.domain.usecase.validation.ConfirmPasswordValidatorUseCase
import com.example.store.domain.usecase.validation.EmailValidatorUseCase
import com.example.store.domain.usecase.validation.PasswordValidatorUseCase
import com.example.store.presentation.event.auth.AuthEvent
import com.example.store.presentation.state.auth.AuthViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val emailValidator: EmailValidatorUseCase,
    private val passwordValidator: PasswordValidatorUseCase,
    private val confirmPasswordValidator: ConfirmPasswordValidatorUseCase
) : ViewModel() {
    private val _registerState = MutableStateFlow(AuthViewState())
    val registerState: StateFlow<AuthViewState> = _registerState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<RegisterUiEvent>()
    val uiEvent: SharedFlow<RegisterUiEvent> get() = _uiEvent

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.Register -> validateForm(
                email = event.email,
                password = event.password,
                confirmPassword = event.confirmPassword
            )

            is AuthEvent.ResetErrorMessage -> updateErrorMessage(message = null)
            else -> {}
        }
    }

    private fun register(email: String, password: String) {
        viewModelScope.launch {
            registerUseCase(email, password).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _registerState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                firebaseUser = resource.data,
                                errorMessage = null
                            )
                        }
                        _uiEvent.emit(RegisterUiEvent.NavigateToLogin(email, password))
                    }

                    is Resource.Error -> updateErrorMessage(resource.errorMessage)

                    is Resource.Loading -> {
                        _registerState.update { currentState ->
                            currentState.copy(isLoading = resource.loading)
                        }
                    }
                }
            }
        }
    }

    private fun validateForm(email: String, password: String, confirmPassword: String) {
        val isEmailValid = emailValidator(email)
        val isPasswordValid = passwordValidator(password)

        val doPasswordsMatch = confirmPasswordValidator(password, confirmPassword)
        val areFieldsValid = listOf(isEmailValid, isPasswordValid).all { it }

        if (!doPasswordsMatch) {
            updateErrorMessage(message = "Passwords do not match!")
            return
        }

        if (!areFieldsValid) {
            updateErrorMessage(message = "Fields are not valid!")
            return
        }

        _registerState.update { it.copy(isLoading = true) }
        register(email = email, password = password)
    }

    private fun updateErrorMessage(message: String?) {
        _registerState.update { currentState -> currentState.copy(errorMessage = message) }
    }

    fun onAlreadyHaveAccountClicked() {
        viewModelScope.launch {
            _uiEvent.emit(RegisterUiEvent.AlreadyHaveAccountNavigation)
        }
    }

    sealed interface RegisterUiEvent {
        data object AlreadyHaveAccountNavigation : RegisterUiEvent
        data class NavigateToLogin(val email: String, val password: String) : RegisterUiEvent
    }
}