package com.example.store.presentation.screen.log_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.common.Resource
import com.example.store.domain.usecase.log_in.GoogleSignInUseCase
import com.example.store.domain.usecase.log_in.LogInUseCase
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
class LogInViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase,
    private val emailValidator: EmailValidatorUseCase,
    private val passwordValidator: PasswordValidatorUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {

    private val _logInState = MutableStateFlow(AuthViewState())
    val logInState: StateFlow<AuthViewState> = _logInState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<LogInUiEvent>()
    val uiEvent: SharedFlow<LogInUiEvent> get() = _uiEvent

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.LogIn -> validateForm(email = event.email, password = event.password)
            is AuthEvent.ResetErrorMessage -> updateErrorMessage(message = null)
            else -> {}
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            logInUseCase(email, password).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _logInState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                firebaseUser = resource.data,
                                errorMessage = null
                            )
                        }
                        _uiEvent.emit(LogInUiEvent.NavigateToHome)
                    }

                    is Resource.Error -> updateErrorMessage(resource.errorMessage)

                    is Resource.Loading -> {
                        _logInState.update { currentState ->
                            currentState.copy(isLoading = resource.loading)
                        }
                    }
                }
            }
        }
    }

    //    Sign in with google needs implementation
    fun googleLogIn(idToken: String) {
        viewModelScope.launch {
            googleSignInUseCase(idToken).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _logInState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                firebaseUser = resource.data,
                                errorMessage = null
                            )
                        }
                        _uiEvent.emit(LogInUiEvent.NavigateToHome)
                    }

                    is Resource.Error -> updateErrorMessage(resource.errorMessage)

                    is Resource.Loading -> {
                        _logInState.update { currentState ->
                            currentState.copy(isLoading = resource.loading)
                        }
                    }
                }
            }
        }
    }

    private fun validateForm(email: String, password: String) {
        val isEmailValid = emailValidator(email)
        val isPasswordValid = passwordValidator(password)

        val areFieldsValid = listOf(isEmailValid, isPasswordValid).all { it }

        if (!areFieldsValid) {
            updateErrorMessage(message = "Fields are not valid!")
            return
        }

        _logInState.update { it.copy(isLoading = true) }
        login(email = email, password = password)
    }

    private fun updateErrorMessage(message: String?) {
        _logInState.update { currentState -> currentState.copy(errorMessage = message) }
    }

    fun navigateToRegister() {
        viewModelScope.launch {
            _uiEvent.emit(LogInUiEvent.NavigateToRegister)
        }
    }

    sealed interface LogInUiEvent {
        data object NavigateToRegister : LogInUiEvent
        data object NavigateToHome : LogInUiEvent
    }
}
