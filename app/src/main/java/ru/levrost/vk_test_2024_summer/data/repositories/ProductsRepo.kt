package ru.levrost.vk_test_2024_summer.data.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import ru.levrost.vk_test_2024_summer.data.dataSources.retrofit.ApiClient
import ru.levrost.vk_test_2024_summer.data.dataSources.retrofit.ServerApi
import ru.levrost.vk_test_2024_summer.data.model.Product

class ProductsRepo {
    private val serverApi: ServerApi = ApiClient.retrofit.create(ServerApi::class.java)
    suspend fun getProductList(skip: Int): List<Product>{
        return withContext(Dispatchers.IO){
            val response = serverApi.getProducts(skip, 20).execute()
            Log.d("ServerDebugMess", response.body()?.products.toString())
            Log.d("ServerDebugMess", response.body()?.skip.toString())
            Log.d("ServerDebugMess", response.body()?.total.toString())
            if (checkResponseCode(response)){
                return@withContext response.body()?.products ?: emptyList<Product>()
            }
            else return@withContext emptyList<Product>()
        }
    }

    private fun <T> checkResponseCode(response: Response<T>): Boolean{
        return when(response.code()){
            200 -> true
            else -> false
        }
    }
}
