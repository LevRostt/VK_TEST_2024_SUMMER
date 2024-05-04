package ru.levrost.vk_test_2024_summer.data.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response
import ru.levrost.vk_test_2024_summer.data.dataSources.retrofit.ApiClient
import ru.levrost.vk_test_2024_summer.data.dataSources.retrofit.ServerApi
import ru.levrost.vk_test_2024_summer.data.model.Product

class ProductsRepo {
    private val serverApi: ServerApi = ApiClient.retrofit.create(ServerApi::class.java)

    suspend fun getProductList(skip: Int): Flow<List<Product>> = flow {
        val response = serverApi.getProducts(skip, 20)
        Log.d("ServerDebugMess", response.body()?.products.toString())
        Log.d("ServerDebugMess", response.body()?.skip.toString())
        Log.d("ServerDebugMess", response.body()?.total.toString())
        if (checkResponseCode(response))
            emit(response.body()?.products ?: emptyList())
        else
            emit(emptyList())
    }
        .flowOn(Dispatchers.IO)


    private fun <T> checkResponseCode(response: Response<T>): Boolean{
        return when(response.code()){
            200 -> true
            else -> false
        }
    }
}
