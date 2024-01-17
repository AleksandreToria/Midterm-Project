package com.example.store.presentation.screen.product_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.common.Resource
import com.example.store.domain.usecase.product.GetProductInfoUseCase
import com.example.store.presentation.event.product.ProductEvent
import com.example.store.presentation.mapper.product.toPresenter
import com.example.store.presentation.state.product.ProductState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductInfoViewModel @Inject constructor(
    private val getProductInfoUseCase: GetProductInfoUseCase
) : ViewModel() {
    private val _productInfoState = MutableStateFlow(ProductState())
    val productInfoState: StateFlow<ProductState> = _productInfoState.asStateFlow()

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.FetchProductInfo -> fetchProducts(event.id)
            is ProductEvent.ResetErrorMessage -> updateErrorMessage(null)
            else -> {}
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
}