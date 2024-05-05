package ru.levrost.vk_test_2024_summer.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import ru.levrost.vk_test_2024_summer.data.model.Product
import ru.levrost.vk_test_2024_summer.data.repositories.ProductsRepo
import kotlin.coroutines.CoroutineContext

class MainViewModel: ViewModel() {
    private val repo = ProductsRepo()

    fun getProductList(): StateFlow<QueryResult>{
        val products = MutableStateFlow<QueryResult>(QueryResult.Success(emptyList()))
        viewModelScope.launch {
            repo.getProductList()
                .catch {
                    products.value = QueryResult.Error(it)
                }
                .collect {
                    products.value = QueryResult.Success(it)
                }
        }
        return products
    }

    fun nextPage(currentPosition: Int) {
        repo.nextPage(currentPosition)
    }

    sealed class QueryResult {
        data class Success(val products: List<Product>): QueryResult()
        data class Error(val exception: Throwable): QueryResult()
    }
}