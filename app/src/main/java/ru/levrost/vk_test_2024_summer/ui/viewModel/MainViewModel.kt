package ru.levrost.vk_test_2024_summer.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.levrost.vk_test_2024_summer.data.model.Product
import ru.levrost.vk_test_2024_summer.data.repositories.ProductsRepo

class MainViewModel: ViewModel() {
    private val repo = ProductsRepo()
    fun getProductList(): List<Product>{
        viewModelScope.launch {
            repo.getProductList(0)
        }
        return emptyList()
    }
}