package com.example.store.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.common.Resource
import com.example.store.domain.remote.usecase.category.GetCategoryUseCase
import com.example.store.domain.remote.usecase.category_product.GetCategoryProductUseCase
import com.example.store.domain.remote.usecase.product.GetProductUseCase
import com.example.store.presentation.event.home.HomeEvent
import com.example.store.presentation.mapper.product.toPresenter
import com.example.store.presentation.model.product.Product
import com.example.store.presentation.state.product.ProductState
import com.google.firebase.auth.FirebaseAuth
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

    private var isFetchingByCategory = false
    private var fullProductList = listOf<Product>()
    private var fullCategoryProductList = listOf<Product>()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.FetchProducts -> fetchProducts()
            is HomeEvent.ResetErrorMessage -> updateErrorMessage(null)
            is HomeEvent.ItemClick -> onClick(HomeFragmentUiEvent.NavigateToProductInfo(event.id))
            is HomeEvent.FetchCategories -> fetchCategories()
            is HomeEvent.FetchProductsByCategory -> fetchProductsByCategory(event.category)
            is HomeEvent.SearchProducts -> searchProducts(event.title)
            is HomeEvent.SignOut -> signOut(HomeFragmentUiEvent.NavigateToLogin)
        }
    }

    private fun fetchProducts() {
        isFetchingByCategory = false
        viewModelScope.launch {
            getProductUseCase().collect { it ->
                when (it) {
                    is Resource.Success -> {
                        fullProductList = it.data.map { it.toPresenter() }
                        _productState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                products = fullProductList
                            )
                        }
                    }

                    is Resource.Error -> updateErrorMessage(it.errorMessage)
                    is Resource.Loading -> {
                        _productState.update { currentState ->
                            currentState.copy(isLoading = it.loading)
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
        isFetchingByCategory = true
        viewModelScope.launch {
            getCategoryProductUseCase(category).collect { it ->
                when (it) {
                    is Resource.Error -> updateErrorMessage(it.errorMessage)
                    is Resource.Loading -> _productState.update {
                        it.copy(isLoading = it.isLoading)
                    }

                    is Resource.Success -> {
                        fullCategoryProductList = it.data.map { it.toPresenter() }
                        _productState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                categoryProducts = fullCategoryProductList
                            )
                        }
                    }
                }
            }
        }
    }

    private fun searchProducts(title: String) {
        val filteredProducts = if (title.isEmpty()) {
            if (isFetchingByCategory) {
                fullCategoryProductList
            } else {
                fullProductList
            }
        } else {
            val productListToSearch = if (isFetchingByCategory) {
                fullCategoryProductList
            } else {
                fullProductList
            }
            productListToSearch.filter { it.title.contains(title, ignoreCase = true) }
        }

        _productState.update { currentState ->
            if (isFetchingByCategory) {
                currentState.copy(
                    categoryProducts = filteredProducts,
                    products = filteredProducts
                )
            } else {
                currentState.copy(
                    products = filteredProducts
                )
            }
        }
    }

    private fun onClick(homeFragmentUiEvent: HomeFragmentUiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(homeFragmentUiEvent)
        }
    }

    private fun signOut(homeFragmentUiEvent: HomeFragmentUiEvent) {
        auth.signOut()
        viewModelScope.launch {
            _uiEvent.emit(homeFragmentUiEvent)
        }
    }

    fun isFetchingByCategory() = isFetchingByCategory

    private fun updateErrorMessage(message: String?) {
        _productState.update { currentState -> currentState.copy(errorMessage = message) }
    }

    sealed interface HomeFragmentUiEvent {
        data class NavigateToProductInfo(val id: Int) : HomeFragmentUiEvent
        data object NavigateToCart : HomeFragmentUiEvent
        data object NavigateToLogin: HomeFragmentUiEvent
    }
}