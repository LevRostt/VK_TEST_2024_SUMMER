package ru.levrost.vk_test_2024_summer.data.dataSources.retrofit.model

import ru.levrost.vk_test_2024_summer.data.model.Product

data class ProductsServerRequest(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int
)