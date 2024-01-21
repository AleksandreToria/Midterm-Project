package com.example.store.presentation.event.product_info

import com.example.store.domain.local.model.CartEntity

sealed class ProductInfoEvent {
    data class AddItem(val cartEntity: CartEntity) : ProductInfoEvent()
    data class FetchProductInfo(val id: Int) : ProductInfoEvent()
    data object ResetErrorMessage : ProductInfoEvent()
}