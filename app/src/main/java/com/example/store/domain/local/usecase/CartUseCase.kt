package com.example.store.domain.local.usecase

import javax.inject.Inject

data class CartUseCase @Inject constructor(
    val addItem: AddItemUseCase,
    val removeItem: RemoveItemUseCase,
    val getAllItems: GetAllItemsUseCase,
    val removeAllItemsUseCase: RemoveAllItemsUseCase
)
