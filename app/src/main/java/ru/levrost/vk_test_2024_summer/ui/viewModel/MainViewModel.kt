package ru.levrost.vk_test_2024_summer.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.levrost.vk_test_2024_summer.data.model.Product
import ru.levrost.vk_test_2024_summer.data.repositories.ProductsRepo

class MainViewModel: ViewModel() {
    private val repo = ProductsRepo()
    private var skip = 0
    fun getProductList(): StateFlow<QueryResult>{
        val products = MutableStateFlow<QueryResult>(QueryResult.Success(emptyList()))
        viewModelScope.launch {
            repo.getProductList(skip)
                .catch {
                    products.value = QueryResult.Error(it)
                }
                .collect {
                    products.value = QueryResult.Success(it)
                }
        }
        return products
    }

    fun nextPage() {
        skip += 20
    }

    sealed class QueryResult {
        data class Success(val products: List<Product>): QueryResult()
        data class Error(val exception: Throwable): QueryResult()
    }
}