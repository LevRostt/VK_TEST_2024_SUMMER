package ru.levrost.vk_test_2024_summer.data.dataSources.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.levrost.vk_test_2024_summer.data.model.ProductsRequest

interface ServerApi {
    @GET("products")
    suspend fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ) : Response<ProductsRequest>
}