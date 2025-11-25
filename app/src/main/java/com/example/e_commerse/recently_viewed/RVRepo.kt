package com.example.e_commerse.recently_viewed

class RVRepo(private val dao: ProductDao) {

    suspend fun addProduct(entity: ProductEntity) {
        dao.insertProduct(entity)
        dao.cleanupOldItems()
    }

    suspend fun getRecentProducts(): List<ProductEntity> {
        return dao.getRecentProducts()
    }
}
