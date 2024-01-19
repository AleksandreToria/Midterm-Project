package com.example.store.data.service

import retrofit2.Response
import retrofit2.http.GET

interface CategoryService {
    @GET("products/categories")
    suspend fun getCategories(): Response<List<String>>
}