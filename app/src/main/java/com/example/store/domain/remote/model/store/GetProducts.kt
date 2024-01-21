package com.example.store.domain.remote.model.store

import kotlinx.serialization.Serializable

@Serializable
data class GetProducts(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val rating: Rating,
    val title: String
)