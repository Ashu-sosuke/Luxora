package com.example.e_commerse


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(productId: String, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var product by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(productId) {
        db.collection("products").document(productId)
            .get()
            .addOnSuccessListener { doc ->
                product = doc.toObject(Product::class.java)
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Detail", color = NeonGreen) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.DarkGray)
            )
        },
        containerColor = MatteBlack
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else {
            product?.let {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    AsyncImage(
                        model = it.imgUrl,
                        contentDescription = it.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(it.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("₹${it.price}", fontSize = 18.sp, color = NeonGreen)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(it.description ?: "No description available.", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { /* TODO: Add to cart */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                    ) {
                        Text("Add to Cart", color = Color.Black)
                    }
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Product not found", color = Color.Red)
            }
        }
    }
}
