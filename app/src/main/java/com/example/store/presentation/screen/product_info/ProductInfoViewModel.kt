package com.example.store.presentation.screen.product_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.common.Resource
import com.example.store.domain.local.model.CartEntity
import com.example.store.domain.local.usecase.CartUseCase
import com.example.store.domain.remote.usecase.product.GetProductInfoUseCase
import com.example.store.presentation.event.product_info.ProductInfoEvent
import com.example.store.presentation.mapper.product.toPresenter
import com.example.store.presentation.state.product.ProductState
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
class ProductInfoViewModel @Inject constructor(
    private val getProductInfoUseCase: GetProductInfoUseCase,
    private val cartUseCase: CartUseCase
) : ViewModel() {
    private val _productInfoState = MutableStateFlow(ProductState())
    val productInfoState: StateFlow<ProductState> = _productInfoState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ProductInfoUiEvent>()
    val uiEvent: SharedFlow<ProductInfoUiEvent> get() = _uiEvent

    fun onEvent(event: ProductInfoEvent) {
        when (event) {
            is ProductInfoEvent.FetchProductInfo -> fetchProducts(event.id)
            is ProductInfoEvent.ResetErrorMessage -> updateErrorMessage(null)
            is ProductInfoEvent.AddItem -> addItem(event.cartEntity)
        }
    }

    private fun fetchProducts(id: Int) {
        viewModelScope.launch {
            getProductInfoUseCase(id).collect {
                when (it) {
                    is Resource.Success -> {
                        _productInfoState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                productInfo = it.data.toPresenter()
                            )
                        }
                    }

                    is Resource.Error -> updateErrorMessage(it.errorMessage)

                    is Resource.Loading -> {
                        _productInfoState.update { currentState ->
                            currentState.copy(
                                isLoading = it.loading
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateErrorMessage(message: String?) {
        _productInfoState.update { currentState -> currentState.copy(errorMessage = message) }
    }

    fun onNavigateToHomeScreen() {
        viewModelScope.launch {
            _uiEvent.emit(ProductInfoUiEvent.NavigateToHomeScreen)
        }
    }

    fun onNavigateToCart() {
        viewModelScope.launch {
            _uiEvent.emit(ProductInfoUiEvent.NavigateToCart)
        }
    }

    private fun addItem(item: CartEntity) {
        viewModelScope.launch {
            cartUseCase.addItem(item)
        }
    }

    fun getCurrentProductId(): Int? {
        return _productInfoState.value.productInfo?.id
    }

    fun getCurrentImage(): String? {
        return _productInfoState.value.productInfo?.image
    }

    sealed interface ProductInfoUiEvent {
        data object NavigateToHomeScreen : ProductInfoUiEvent
        data object NavigateToCart : ProductInfoUiEvent
    }
}