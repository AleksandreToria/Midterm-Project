package com.example.store.presentation.event.cart


sealed class CartEvent {
    data object GetItems : CartEvent()
    data object RemoveAllItems : CartEvent()
    data object NavigateHome : CartEvent()
    data object CalculateTotalPrice : CartEvent()
}