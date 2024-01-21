package com.example.store.data.remote.network.model.store

data class GetProductsDto(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val rating: RatingDto,
    val title: String
)