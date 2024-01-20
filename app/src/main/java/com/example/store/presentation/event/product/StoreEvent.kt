package com.example.store.presentation.event.product

sealed class StoreEvent {
    data object FetchProducts : StoreEvent()
    data object FetchCategories : StoreEvent()
    data class FetchProductsByCategory(val category: String) : StoreEvent()
    data class FetchStoreInfo(val id: Int) : StoreEvent()
    data object ResetErrorMessage : StoreEvent()
    data class ItemClick(val id: Int) : StoreEvent()
    data class SearchProducts(val title: String) : StoreEvent()
}