package ru.levrost.vk_test_2024_summer.data.dataSources.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.levrost.vk_test_2024_summer.data.dataSources.retrofit.model.ServerProductsRequest

interface ServerApi {
    @GET("products")
    suspend fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ) : Response<ServerProductsRequest>

    @GET("products/categories")
    suspend fun getCategoriesList() : Response<List<String>>

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(
        @Path("category") category: String,
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ) : Response<ServerProductsRequest>
}