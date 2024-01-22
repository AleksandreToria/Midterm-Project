package com.example.store.presentation.event.home

sealed class HomeEvent {
    data object FetchProducts : HomeEvent()
    data object FetchCategories : HomeEvent()
    data class FetchProductsByCategory(val category: String) : HomeEvent()
    data object ResetErrorMessage : HomeEvent()
    data class ItemClick(val id: Int) : HomeEvent()
    data class SearchProducts(val title: String) : HomeEvent()
    data object SignOut: HomeEvent()
}