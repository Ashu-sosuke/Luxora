package com.example.e_commerse

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow

class ProductViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    fun fetchProducts(mainCategory: String, subCategory: String) {
        if (mainCategory.isBlank() || subCategory.isBlank()) {
            println("⚠️ Invalid category parameters")
            _products.value = emptyList()
            return
        }

        db.collection("products")
            .whereEqualTo("mainCategory", mainCategory)
            .whereEqualTo("subCategory", subCategory)
            .get()
            .addOnSuccessListener { result ->
                _products.value = result.documents.mapNotNull {
                    it.toObject(Product::class.java)?.copy(id = it.id)
                }
                println("✅ Loaded ${_products.value.size} products from Firestore")
            }
            .addOnFailureListener { e ->
                println("❌ Firestore error: ${e.message}")
            }
    }


    fun getProductById(id: String): Flow<Product?> = callbackFlow {
        val listener = db.collection("products").document(id)
            .addSnapshotListener { doc, _ ->
                trySend(doc?.toObject(Product::class.java)?.copy(id = id))
            }
        awaitClose { listener.remove() }
    }
}