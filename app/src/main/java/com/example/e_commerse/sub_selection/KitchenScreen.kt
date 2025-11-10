package com.example.e_commerse.sub_selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.example.e_commerse.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitchenScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var kitchenProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        db.collection("products")
            .whereEqualTo("mainCategory", "Kitchen")
            .get()
            .addOnSuccessListener { snapshot ->
                kitchenProducts = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Product::class.java)?.copy(id = doc.id)
                }
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    Scaffold(
        containerColor = MatteBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Kitchen",
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Gray)
            )
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NeonGreen)
                }
            }

            kitchenProducts.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Kitchen Products Found", color = NeonGreen)
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(innerPadding)
                        .background(MatteBlack)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(kitchenProducts) { product ->
                        KitchenProductCard(product = product) {
                            navController.navigate("product_detail/${product.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KitchenProductCard(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp)),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1C)),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Column {
            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = product.name,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = NeonGreen,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Text(
                text = "₹${product.price}",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}
