package ru.levrost.vk_test_2024_summer.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.levrost.vk_test_2024_summer.data.model.Product
import ru.levrost.vk_test_2024_summer.data.repositories.ProductsRepo
import ru.levrost.vk_test_2024_summer.debugLog

class MainViewModel: ViewModel() {
    private val repo = ProductsRepo()
    private val _category = MutableStateFlow<List<String>>(emptyList())
    var currentFilter : String = ""
    var currentQuery : String = ""
    val category : StateFlow<List<String>> get() = _category

    init {
        getLatestCategory()
    }

    private var callableStateFlowAgain = false
    fun getLatestCategory(){
        viewModelScope.launch {
            repo.getCategoryList()
                .catch {
                    callableStateFlowAgain = true
                }
                .collect{
                    _category.value = it
                    callableStateFlowAgain = false
                }
        }
    }


    fun getProducts(skip: Int): StateFlow<QueryResult>{
        val products = MutableStateFlow<QueryResult>(QueryResult.Success(emptyList()))

        if (callableStateFlowAgain)
            getLatestCategory()

        viewModelScope.launch {
            repo.getProducts(skip)
                .catch {
                    products.value = QueryResult.Error(it)
                }
                .collectLatest {
                    products.value = QueryResult.Success(it)
                }
        }
        return products
    }

    fun getFilterProducts(skip: Int): StateFlow<QueryResult>{
        val products = MutableStateFlow<QueryResult>(QueryResult.Success(emptyList()))

        viewModelScope.launch {
            if (currentFilter == "")
                getProducts(skip)
            else
                repo.getFilterProducts(currentFilter, skip)
                    .catch {
                        products.value = QueryResult.Error(it)
                    }
                    .collectLatest {
                        products.value = QueryResult.Success(it)
                    }
        }
        return products
    }

    fun getSearchedProducts(skip: Int) : StateFlow<QueryResult>{
        val products = MutableStateFlow<QueryResult>(QueryResult.Success(emptyList()))

        viewModelScope.launch {
            if (currentQuery == "")
                getProducts(skip)
            else
                repo.getSearchProducts(currentQuery, skip)
                    .catch {
                        products.value = QueryResult.Error(it)
                    }
                    .collectLatest {
                        products.value = QueryResult.Success(it)
                    }
        }
        return products
    }

    sealed class QueryResult {
        data class Success(val products: List<Product>): QueryResult()
        data class Error(val exception: Throwable): QueryResult()
    }
}