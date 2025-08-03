package com.example.e_commerse

data class Product(
    val id: String = "",
    val name: String = "",
    val imgUrl: String = "",
    val price: Double = 0.0,
    val description: String? = null,
    val mainCategory: String = "",
    val subCategory: String = ""
)
