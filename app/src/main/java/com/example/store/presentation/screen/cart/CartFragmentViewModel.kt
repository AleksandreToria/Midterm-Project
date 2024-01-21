package com.example.store.presentation.screen.cart

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.domain.local.usecase.CartUseCase
import com.example.store.presentation.event.cart.CartEvent
import com.example.store.presentation.state.cart.CartState
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
class CartFragmentViewModel @Inject constructor(
    private val cartUseCase: CartUseCase
) : ViewModel() {
    private val _cartState = MutableStateFlow(CartState())
    val cartState: StateFlow<CartState> = _cartState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<CartUiEvent>()
    val uiEvent: SharedFlow<CartUiEvent> get() = _uiEvent

    fun onEvent(event: CartEvent) {
        when (event) {
            CartEvent.GetItems -> getItems()
            CartEvent.RemoveAllItems -> clearCart()
        }
    }

    private fun getItems() {
        viewModelScope.launch {
            try {
                _cartState.update {
                    it.copy(
                        allItems = cartUseCase.getAllItems()
                    )
                }
            } catch (e: Exception) {
                d("ylaa", "${e.message}")
                updateErrorMessage(e.message)
            }
        }
    }

    private fun clearCart() {
        viewModelScope.launch {
            try {
                cartUseCase.removeAllItemsUseCase()
                _uiEvent.emit(CartUiEvent.NavigateToHomeFragment)
            } catch (e: Exception) {
                d("ylaa", "${e.message}")
                updateErrorMessage(e.message)
            }
        }
    }

    private fun updateErrorMessage(message: String?) {
        _cartState.update { currentState -> currentState.copy(errorMessage = message) }
    }

    sealed interface CartUiEvent {
        data object NavigateToHomeFragment : CartUiEvent
    }
}