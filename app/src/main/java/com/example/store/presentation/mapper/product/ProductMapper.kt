package com.example.store.presentation.mapper.product

import com.example.store.domain.remote.model.store.GetProducts
import com.example.store.domain.remote.model.store.Rating
import com.example.store.presentation.model.product.Product
import com.example.store.presentation.model.product.Rate

fun GetProducts.toPresenter() =
    Product(
        category = category,
        description = description,
        id = id,
        image = image,
        price = price,
        rating = rating.ratingToPresent(),
        title = title
    )

fun Rating.ratingToPresent() =
    Rate(
        rate = rate,
        count = count
    )
