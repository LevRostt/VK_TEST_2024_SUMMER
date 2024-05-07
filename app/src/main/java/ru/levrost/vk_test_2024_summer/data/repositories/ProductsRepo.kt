package ru.levrost.vk_test_2024_summer.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import ru.levrost.vk_test_2024_summer.data.dataSources.retrofit.ApiClient
import ru.levrost.vk_test_2024_summer.data.dataSources.retrofit.ServerApi
import ru.levrost.vk_test_2024_summer.data.dataSources.retrofit.model.ServerProductsRequest
import ru.levrost.vk_test_2024_summer.data.model.Product
import ru.levrost.vk_test_2024_summer.debugLog

class ProductsRepo(val dispatcher: CoroutineDispatcher = Dispatchers.IO) {
    private val serverApi: ServerApi = ApiClient.retrofit.create(ServerApi::class.java)
    private var maxSkip = 0

    suspend fun getProducts(skip: Int): Flow<List<Product>> =
        fetchProducts { serverApi.getProducts(skip, 20) }

    suspend fun getFilterProducts(filter: String, skip: Int): Flow<List<Product>> =
        fetchProducts { serverApi.getProductsByCategory(filter, skip, 20) }

    suspend fun getSearchProducts(query: String, skip: Int): Flow<List<Product>> =
        fetchProducts { serverApi.getProductsBySearch(query, skip, 20) }

    private suspend fun fetchProducts(
        request: suspend () -> Response<ServerProductsRequest>,
    ): Flow<List<Product>> = flow {
        val response = request()

        if (checkResponseCode(response)){
            oldListChecker(response.body()!!.total)
            emit(response.body()?.products ?: emptyList())
        } else {
            emit(emptyList())
        }

    }.flowOn(dispatcher)

    private fun oldListChecker(total: Int){
        if (maxSkip != total){
            maxSkip = total
        }
    }

    suspend fun getCategoryList(): Flow<List<String>> = flow {
        val response = serverApi.getCategoriesList()
        response.body()?.let { emit(it) }
    }
        .flowOn(dispatcher)

    private fun <T> checkResponseCode(response: Response<T>): Boolean{
        return when(response.code()){
            200 -> true
            else -> false
        }
    }
}
