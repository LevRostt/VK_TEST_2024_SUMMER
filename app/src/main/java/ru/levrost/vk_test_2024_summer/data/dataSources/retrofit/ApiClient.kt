package ru.levrost.vk_test_2024_summer.data.dataSources.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    const val URL = "https://dummyjson.com/"
//    val client = OkHttpClient.Builder() //for debugging
//        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}