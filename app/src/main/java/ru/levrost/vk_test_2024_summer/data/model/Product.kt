package ru.levrost.vk_test_2024_summer.data.model

data class Product(
    val id: Long,
    val title: String,
    val description: String,
    val thumbnail: String,
    val rating: Double,
    val discountPercentage: Double,
    val category: String,
    val images: List<String>,
    val brand: String,
    val stock: Int,
    val price: Int
)