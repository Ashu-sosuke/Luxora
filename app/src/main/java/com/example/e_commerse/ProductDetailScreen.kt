package com.example.e_commerse

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.example.e_commerse.MatteBlack
import com.example.e_commerse.NeonGreen
import com.example.e_commerse.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(productId: String, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var product by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // ✅ Real-time listener for product details
    LaunchedEffect(productId) {
        db.collection("products").document(productId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    error.printStackTrace()
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    product = snapshot.toObject(Product::class.java)?.copy(id = snapshot.id)
                    isLoading = false
                }
            }
    }

    Scaffold(
        containerColor = MatteBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = product?.name ?: "Product Details",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            )
        }
    ) { padding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NeonGreen)
                }
            }

            product == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Product not found", color = NeonGreen)
                }
            }

            else -> {
                val p = product!!
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .background(MatteBlack)
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 🔹 Show first image
                    if (p.images.isNotEmpty()) {
                        AsyncImage(
                            model = p.images.first(),
                            contentDescription = p.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Text(p.name, color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("₹${p.price}", color = Color.LightGray, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Stock: ${p.stock}", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    p.description?.let {
                        Text(
                            text = it,
                            color = Color.White,
                            fontSize = 15.sp,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    if (p.affiliateLink.isNotEmpty()) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(p.affiliateLink))
                                navController.context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                        ) {
                            Text("Buy Now", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
