package com.example.store.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.common.Resource
import com.example.store.domain.usecase.category.GetCategoryUseCase
import com.example.store.domain.usecase.category_product.GetCategoryProductUseCase
import com.example.store.domain.usecase.product.GetProductUseCase
import com.example.store.presentation.event.product.StoreEvent
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
class HomeFragmentViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getCategoryProductUseCase: GetCategoryProductUseCase
) : ViewModel() {
    private val _productState = MutableStateFlow(ProductState())
    val productState: StateFlow<ProductState> = _productState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<HomeFragmentUiEvent>()
    val uiEvent: SharedFlow<HomeFragmentUiEvent> get() = _uiEvent

    fun onEvent(event: StoreEvent) {
        when (event) {
            is StoreEvent.FetchProducts -> fetchProducts()
            is StoreEvent.ResetErrorMessage -> updateErrorMessage(null)
            is StoreEvent.ItemClick -> onClick(HomeFragmentUiEvent.NavigateToProductInfo(event.id))
            is StoreEvent.FetchCategories -> fetchCategories()
            is StoreEvent.FetchProductsByCategory -> fetchProductsByCategory(event.category)
            else -> {}
        }
    }

    private fun onClick(homeFragmentUiEvent: HomeFragmentUiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(homeFragmentUiEvent)
        }
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            getProductUseCase().collect { it ->
                when (it) {
                    is Resource.Success -> {
                        _productState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                products = it.data.map { it.toPresenter() }
                            )
                        }
                    }

                    is Resource.Error -> updateErrorMessage(it.errorMessage)

                    is Resource.Loading -> {
                        _productState.update { currentState ->
                            currentState.copy(
                                isLoading = it.loading
                            )
                        }
                    }
                }
            }
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            getCategoryUseCase().collect { it ->
                when (it) {
                    is Resource.Error -> updateErrorMessage(it.errorMessage)
                    is Resource.Loading -> _productState.update {
                        it.copy(isLoading = it.isLoading)
                    }

                    is Resource.Success -> _productState.update { currentState ->
                        currentState.copy(
                            categories = it.data
                        )
                    }
                }
            }
        }
    }

    private fun fetchProductsByCategory(category: String) {
        viewModelScope.launch {
            getCategoryProductUseCase(category).collect { it ->
                when (it) {
                    is Resource.Error -> updateErrorMessage(it.errorMessage)
                    is Resource.Loading -> _productState.update {
                        it.copy(isLoading = it.isLoading)
                    }

                    is Resource.Success -> {
                        _productState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                categoryProducts = it.data.map { it.toPresenter() }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateErrorMessage(message: String?) {
        _productState.update { currentState -> currentState.copy(errorMessage = message) }
    }

    sealed interface HomeFragmentUiEvent {
        data class NavigateToProductInfo(val id: Int) : HomeFragmentUiEvent
    }
}