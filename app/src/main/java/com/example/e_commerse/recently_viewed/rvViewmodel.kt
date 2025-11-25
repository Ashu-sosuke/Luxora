package com.example.e_commerse.recently_viewed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.e_commerse.Product
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.CreationExtras

class RvViewmodel(private val repo: RVRepo) : ViewModel() {

    // Expose domain Product list to UI
    var recentlyVisited by mutableStateOf<List<Product>>(emptyList())
        private set

    fun loadRecentlyVisited() {
        viewModelScope.launch {
            val entities = repo.getRecentProducts()
            recentlyVisited = entities.map { it.toDomain() }
        }
    }

    fun saveRecentlyViewed(entity: ProductEntity) {
        viewModelScope.launch {
            repo.addProduct(entity)
            // Optionally reload after insert
            loadRecentlyVisited()
        }
    }
}

class RVViewModelFactory(private val repo: RVRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(RvViewmodel::class.java)) {
            return RvViewmodel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
