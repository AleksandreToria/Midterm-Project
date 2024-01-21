package com.example.store.domain.local.usecase

import com.example.store.domain.local.repository.CartRepository
import javax.inject.Inject

class RemoveAllItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke() {
        cartRepository.removeAllItems()
    }
}