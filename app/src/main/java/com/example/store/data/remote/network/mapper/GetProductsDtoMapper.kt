package com.example.store.data.remote.network.mapper

import com.example.store.data.remote.network.model.store.GetProductsDto
import com.example.store.data.remote.network.model.store.RatingDto
import com.example.store.domain.remote.model.store.GetProducts
import com.example.store.domain.remote.model.store.Rating

fun GetProductsDto.toDomain(): GetProducts {
    return GetProducts(category, description, id, image, price, rating.toDomain(), title)
}

fun RatingDto.toDomain(): Rating {
    return Rating(count, rate)
}