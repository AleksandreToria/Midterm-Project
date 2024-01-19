package com.example.store.data.service

import com.example.store.data.model.store.GetProductsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("products")
    suspend fun getProducts(): Response<List<GetProductsDto>>

    @GET("products/{id}")
    suspend fun getUserInfo(@Path("id") id: Int): Response<GetProductsDto>

    @GET("products/category/{category}")
    suspend fun getCategoryProducts(@Path("category") category: String): Response<List<GetProductsDto>>
}