package com.example.store.presentation.mapper.product

import com.example.store.domain.model.store.GetProducts
import com.example.store.presentation.model.product.Product

fun GetProducts.toPresenter() =
    Product(
        category = category,
        description = description,
        id = id,
        image = image,
        price = price,
        rating = rating,
        title = title
    )