package com.example.store.domain.local.usecase

import com.example.store.domain.local.model.CartEntity
import com.example.store.domain.local.repository.CartRepository
import javax.inject.Inject

class AddItemUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(item: CartEntity) =
        cartRepository.addItem(item)
}