package com.example.e_commerse

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WishlistRepo {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private fun userId(): String {
        return auth.currentUser?.uid ?: ""
    }

    fun addToWishlist(product: Product, onResult: (Boolean) -> Unit) {

        val uid = userId()
        if (uid.isEmpty()) {
            onResult(false)
            return
        }

        // ✅ SAVE FULL PRODUCT (RECOMMENDED)
        db.collection("users")
            .document(uid)
            .collection("wishlist")
            .document(product.id)
            .set(product) // 🔥 THIS FIXES EVERYTHING
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun removeFromWishlist(productId: String, onResult: (Boolean) -> Unit) {

        val uid = userId()
        if (uid.isEmpty()) {
            onResult(false)
            return
        }

        db.collection("users")
            .document(uid)
            .collection("wishlist")
            .document(productId)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getWishlist(onResult: (List<Product>) -> Unit) {

        val uid = userId()
        if (uid.isEmpty()) {
            onResult(emptyList())
            return
        }

        db.collection("users")
            .document(uid)
            .collection("wishlist")
            .addSnapshotListener { snapshot, error ->

                if (error != null || snapshot == null) {
                    onResult(emptyList())
                    return@addSnapshotListener
                }

                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Product::class.java)
                        ?.copy(id = doc.id)
                }

                onResult(list)
            }
    }

    fun isInWishlist(productId: String, onResult: (Boolean) -> Unit) {

        val uid = userId()
        if (uid.isEmpty()) {
            onResult(false)
            return
        }

        db.collection("users")
            .document(uid)
            .collection("wishlist")
            .document(productId)
            .get()
            .addOnSuccessListener { onResult(it.exists()) }
            .addOnFailureListener { onResult(false) }
    }
}
