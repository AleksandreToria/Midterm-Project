package com.example.store.presentation.event.product

sealed class ProductEvent {
    data object FetchProducts: ProductEvent()
    data object ResetErrorMessage : ProductEvent()
}