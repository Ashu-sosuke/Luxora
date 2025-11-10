package com.example.e_commerse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.example.e_commerse.MatteBlack
import com.example.e_commerse.NeonGreen
import androidx.navigation.NavController
import com.example.e_commerse.Product
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(mainCategory: String, subCategory: String, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // ✅ Real-time Firestore listener with proper disposal
    DisposableEffect(mainCategory, subCategory) {
        val listenerRegistration = db.collection("products")
            .whereEqualTo("mainCategory", mainCategory)
            .whereEqualTo("subCategory", subCategory)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    isLoading = false
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    products = snapshot.documents.mapNotNull {
                        val p = it.toObject(Product::class.java)
                        p?.copy(id = it.id)
                    }
                    isLoading = false
                }
            }

        onDispose {
            listenerRegistration.remove()
        }
    }

    Scaffold(
        containerColor = MatteBlack,
        topBar = {
            TopAppBar(
                title = { Text("$subCategory Items", color = NeonGreen) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NeonGreen)
                    }
                }
            )
        }
    ) { padding ->
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = NeonGreen)
                }
            }

            products.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No products found in this category", color = Color.Gray)
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(padding)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(products) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val encodedId = URLEncoder.encode(
                                        product.id,
                                        StandardCharsets.UTF_8.toString()
                                    )
                                    navController.navigate("product_detail/$encodedId")
                                },
                            colors = CardDefaults.cardColors(containerColor = MatteBlack.copy(alpha = 0.8f))
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AsyncImage(
                                    model = product.images.firstOrNull(),
                                    contentDescription = product.name,
                                    modifier = Modifier
                                        .height(120.dp)
                                        .fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(product.name, color = NeonGreen)
                                Text("₹${product.price}", color = Color.LightGray)
                            }
                        }
                    }
                }
            }
        }
    }
}
