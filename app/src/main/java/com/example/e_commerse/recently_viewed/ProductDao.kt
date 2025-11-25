package com.example.e_commerse.recently_viewed

import androidx.room.*

@Dao
interface ProductDao {

    @Query("SELECT * FROM recent_products ORDER BY timestamp DESC LIMIT 20")
    suspend fun getRecentProducts(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("""
        DELETE FROM recent_products 
        WHERE id NOT IN (
            SELECT id FROM recent_products ORDER BY timestamp DESC LIMIT 20
        )
    """)
    suspend fun cleanupOldItems()
}
