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
import com.example.e_commerse.recently_viewed.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    navController: NavController
) {
    val db = FirebaseFirestore.getInstance()

    // ➜ Inject Recently Viewed system
    val context = navController.context
    val productDao = remember { ProductDatabase.getDatabase(context).productDao() }
    val repo = remember { RVRepo(productDao) }
    val rvViewmodel: RvViewmodel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = RVViewModelFactory(repo)
    )

    var product by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // 🔥 Load single product + save to recently viewed
    LaunchedEffect(productId) {
        db.collection("products").document(productId)
            .get()
            .addOnSuccessListener { snapshot ->
                val loaded = snapshot.toObject(Product::class.java)?.copy(id = snapshot.id)
                product = loaded
                isLoading = false

                // ➜ Save to recently viewed
                loaded?.let {
                    rvViewmodel.saveRecentlyViewed(
                        ProductEntity(
                            id = it.id,
                            name = it.name,
                            price = it.price,
                            imageUrl = it.images.firstOrNull() ?: ""
                        )
                    )
                }
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
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NeonGreen)
                }
            }

            product == null -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
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
