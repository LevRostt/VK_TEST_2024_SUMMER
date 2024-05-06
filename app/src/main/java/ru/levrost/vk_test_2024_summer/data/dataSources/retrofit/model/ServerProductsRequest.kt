package ru.levrost.vk_test_2024_summer.data.dataSources.retrofit.model

import ru.levrost.vk_test_2024_summer.data.model.Product

data class ServerProductsRequest(
    val products: List<Product> = emptyList(),
    val total: Int = 0,
    val skip: Int = 0,
    val limit: Int = 0
)