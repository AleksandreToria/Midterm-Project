package com.example.store.domain.local.usecase

import com.example.store.domain.local.model.CartEntity
import com.example.store.domain.local.repository.CartRepository
import javax.inject.Inject

class GetAllItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): List<CartEntity> =
        cartRepository.getAllItems()
}