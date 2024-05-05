package ru.levrost.vk_test_2024_summer.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response
import ru.levrost.vk_test_2024_summer.data.dataSources.retrofit.ApiClient
import ru.levrost.vk_test_2024_summer.data.dataSources.retrofit.ServerApi
import ru.levrost.vk_test_2024_summer.data.model.Product
import ru.levrost.vk_test_2024_summer.data.model.ProductsRequest
import ru.levrost.vk_test_2024_summer.debugLog

class ProductsRepo {
    private val serverApi: ServerApi = ApiClient.retrofit.create(ServerApi::class.java)
    private var currentSkip = 0
    private var max_element = 0

    suspend fun getProductList(): Flow<List<Product>> = flow {
        val response = serverApi.getProducts(currentSkip, 20)
        max_element = response.body()?.total ?: max_element

        if (checkResponseCode(response)){
            emit(response.body()?.products ?: emptyList())
        }
        else
            emit(emptyList())

    }
        .flowOn(Dispatchers.IO)

    fun nextPage(currentPosition: Int){
        if ((currentPosition - 20 == currentSkip) && currentSkip < max_element)
            currentSkip += 20
    }

    private fun <T> checkResponseCode(response: Response<T>): Boolean{
        return when(response.code()){
            200 -> true
            else -> false
        }
    }
}
