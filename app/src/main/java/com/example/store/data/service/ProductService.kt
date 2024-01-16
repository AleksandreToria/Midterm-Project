package com.example.store.data.service

import com.example.store.data.model.store.GetProductsDto
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {
    @GET("products")
    suspend fun getProducts(): Response<List<GetProductsDto>>
}