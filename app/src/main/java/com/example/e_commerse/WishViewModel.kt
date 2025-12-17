package com.example.e_commerse

import androidx.lifecycle.ViewModel
import com.example.e_commerse.recently_viewed.ProductDatabase
import com.example.e_commerse.recently_viewed.RVRepo
import com.example.e_commerse.recently_viewed.RvViewmodel
import com.example.e_commerse.recently_viewed.RVViewModelFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.FirebaseFirestore

class WishlistViewModel : ViewModel() {

    private val repo = WishlistRepo()

    fun getWishlist(onResult: (List<Product>) -> Unit) {
        repo.getWishlist(onResult)
    }

    fun removeFromWishlist(productId: String) {
        repo.removeFromWishlist(productId) {}
    }

    fun addToWishlist(product: Product) {
        repo.addToWishlist(product) {}
    }
}


class SearchViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun searchProducts(
        query: String,
        onResult: (List<Product>) -> Unit
    ) {
        if (query.isBlank()) {
            onResult(emptyList())
            return
        }

        db.collection("products")
            .orderBy("name")
            .startAt(query)
            .endAt(query + "\uf8ff")
            .get()
            .addOnSuccessListener { snapshot ->
                onResult(snapshot.toObjects(Product::class.java))
            }
    }
}
