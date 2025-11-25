package com.example.e_commerse.recently_viewed

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.e_commerse.Product

@Entity(tableName = "recent_products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val timestamp: Long = System.currentTimeMillis()
)

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        price = price,
        images = listOf(imageUrl)
    )
}
