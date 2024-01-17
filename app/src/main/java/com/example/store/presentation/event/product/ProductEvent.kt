package com.example.store.presentation.event.product

sealed class ProductEvent {
    data object FetchProducts : ProductEvent()
    data class FetchProductInfo(val id: Int) : ProductEvent()
    data object ResetErrorMessage : ProductEvent()
}