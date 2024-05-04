package ru.levrost.vk_test_2024_summer.data.dataSources.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.levrost.vk_test_2024_summer.data.dataSources.retrofit.model.ProductsServerRequest

interface ServerApi {
    @GET("products")
    fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ) : Call<ProductsServerRequest>
}