package com.example.e_commerse.sub_selection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.e_commerse.BottomNavBar
import com.example.e_commerse.LightBlue
import com.example.e_commerse.LightBlueGradient
import com.example.e_commerse.Product
import com.example.e_commerse.R
import com.example.e_commerse.Screen
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

val MatteBlack = Color(0xFF121212)
val NeonGreen = Color(0xFFA6E22E)

data class cloth(val imgUrl: String, val name: String)

val MensCothes = listOf(
    cloth("https://images.unsplash.com/photo-1621072156002-e2fccdc0b176?q=80&w=687&auto=format&fit=crop", "Shirt"),
    cloth("https://images.pexels.com/photos/1007864/pexels-photo-1007864.jpeg", "T-Shirt"),
    cloth("https://images.pexels.com/photos/1804075/pexels-photo-1804075.jpeg", "Jeans"),
    cloth("https://images.pexels.com/photos/33088117/pexels-photo-33088117.jpeg", "Kurta-Pyajama")
)

val WomensClothes = listOf(
    cloth("https://images.pexels.com/photos/2784078/pexels-photo-2784078.jpeg", "Saree"),
    cloth("https://images.pexels.com/photos/22431192/pexels-photo-22431192.jpeg", "Kurti"),
    cloth("https://images.pexels.com/photos/33152120/pexels-photo-33152120.jpeg", ("Tank Top")),
    cloth("https://images.pexels.com/photos/4171763/pexels-photo-4171763.jpeg", "Maxi")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FashionScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()

    var mensProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var womensProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // Fetch Men's products
        db.collection("products")
            .whereEqualTo("mainCategory", "Fashion")
            .whereEqualTo("subCategory", "Men")
            .get()
            .addOnSuccessListener { snapshot ->
                mensProducts = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Product::class.java)?.copy(id = doc.id)
                }
                isLoading = false
            }
            .addOnFailureListener { isLoading = false }

        // Fetch Women's products
        db.collection("products")
            .whereEqualTo("mainCategory", "Fashion")
            .whereEqualTo("subCategory", "Women")
            .get()
            .addOnSuccessListener { snapshot ->
                womensProducts = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Product::class.java)?.copy(id = doc.id)
                }
                isLoading = false
            }
            .addOnFailureListener { isLoading = false }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Fashion",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = LightBlueGradient
                )
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .background(LightBlue)
                    .padding(12.dp)
            ) {
                if (mensProducts.isNotEmpty()) {
                    Text(
                        "Men's Collection",
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ProductGrid(mensProducts) { product ->
                        navController.navigate("product_detail/${product.id}")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (womensProducts.isNotEmpty()) {
                    Text(
                        "Women's Collection",
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ProductGrid(womensProducts) { product ->
                        navController.navigate("product_detail/${product.id}")
                    }
                }
            }
        }
    }
}
@Composable
fun ProductGrid(products: List<Product>, onItemClick: (Product) -> Unit) {
    Column {
        for (chunk in products.chunked(2)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                chunk.forEach { product ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .clickable { onItemClick(product) }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            AsyncImage(
                                model = product.images.firstOrNull(),
                                contentDescription = product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = product.name,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Fill empty space if odd number of products
                if (chunk.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
