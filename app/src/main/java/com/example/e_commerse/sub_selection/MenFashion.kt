package com.example.e_commerse.sub_selection

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.e_commerse.BottomNavBar
import com.example.e_commerse.NeonGreen
import com.example.e_commerse.Product
import com.example.e_commerse.R
import com.example.e_commerse.Screen
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay

// Masculine theme colors
val DeepNavy = Color(0xFF0A1A2F)
val SteelGray = Color(0xFF1F2A37)
val GunMetal = Color(0xFF2B3A4A)
val NeonBlue = Color(0xFF4DA8FF)
val MatteWhite = Color(0xFFF5F7FA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenFashionScreen(navController: NavController) {

    val bottomItems = listOf(
        Screen.HomeScreen,
        Screen.ExploreScreen,
        Screen.WishlistScreen,
        Screen.ProfileScreen
    )

    val bottomIcons = listOf(
        R.drawable.outline_home_24,
        R.drawable.expolre,
        R.drawable.icons8_heart_50,
        R.drawable.outline_person_4_24
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val db = FirebaseFirestore.getInstance()

    var mensProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
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
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Men's Fashion",
                        color = MatteWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = NeonBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF854836)
                )
            )
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                currentRoute = currentRoute,
                bottomItems = bottomItems,
                bottomIcons = bottomIcons,
                backgroundColor = Color(0xFFFFF2D7)
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
            Box(
                Modifier
                    .background(Color(0xFFF9F8F6))
                    .fillMaxSize()
            ){
                if (mensProducts.isNotEmpty()){
                    Text(
                        "Women's Collection",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    ProductGrid(
                        products = mensProducts,
                        onItemClick = { product ->
                            navController.navigate("product_detail/${product.id}")
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

    }
}

@Composable
fun ProductGrid(
    products: List<Product>,
    onItemClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(products) { product ->
            ProductCard(product, onItemClick, cardColor = Color(0xFF1E1E2E))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductCard(product: Product, onItemClick: (Product) -> Unit, cardColor: Color) {

    val images = product.images
    val pagerState = rememberPagerState(pageCount = { Int.MAX_VALUE })

    fun realIndex(page: Int): Int = page % images.size



    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onItemClick(product) },
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFE9E3))
    ) {

        Column {

            // Image slider
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) { page ->

                AsyncImage(
                    model = images[realIndex(page)],
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Dot indicators
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(images.size) { index ->
                    val isSelected = realIndex(pagerState.currentPage) == index
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 10.dp else 6.dp)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) NeonBlue else Color.Gray)
                    )
                }
            }

            // 🔽 Product details
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = product.name,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "₹${product.price}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7B542F)
                )
            }
        }
    }
}

