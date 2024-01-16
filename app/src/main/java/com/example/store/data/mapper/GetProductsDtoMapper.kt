package com.example.store.data.mapper

import com.example.store.data.model.store.GetProductsDto
import com.example.store.data.model.store.RatingDto
import com.example.store.domain.model.store.GetProducts
import com.example.store.domain.model.store.Rating

fun GetProductsDto.toDomain(): GetProducts {
    return GetProducts(category, description, id, image, price, rating.ratingToDomain(), title)
}

fun RatingDto.ratingToDomain(): Rating {
    return Rating(count, rate)
}