package com.example.e_commerse

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val mainCategory: String = "",
    val subCategory: String = "",
    val type: String = "",
    val stock: Int = 0,
    val affiliateLink: String = "",
    val images: List<String> = emptyList(),
    val description: String? = null,
    val createdAt: com.google.firebase.Timestamp? = null)
