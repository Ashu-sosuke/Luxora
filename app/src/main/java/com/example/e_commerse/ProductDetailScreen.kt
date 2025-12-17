package com.example.e_commerse

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.example.e_commerse.recently_viewed.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    navController: NavController
) {
    val db = FirebaseFirestore.getInstance()

    val context = navController.context
    val productDao = remember { ProductDatabase.getDatabase(context).productDao() }
    val repo = remember { RVRepo(productDao) }
    val rvViewmodel: RvViewmodel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = RVViewModelFactory(repo)
    )

    var isWishlisted by remember { mutableStateOf(false) }

    val wishlistRepo = remember { WishlistRepo() }
    var product by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(productId) {
        db.collection("products").document(productId)
            .get()
            .addOnSuccessListener { snapshot ->
                val loaded = snapshot.toObject(Product::class.java)?.copy(id = snapshot.id)
                product = loaded
                isLoading = false

                loaded?.let {
                    rvViewmodel.saveRecentlyViewed(
                        ProductEntity(
                            id = it.id,
                            name = it.name,
                            price = it.price,
                            imageUrl = it.images.firstOrNull() ?: ""
                        )
                    )

                    wishlistRepo.isInWishlist(it.id) { exists ->
                        isWishlisted = exists
                    }
                }
            }
            .addOnFailureListener {
                isLoading = false
            }
    }


    Scaffold(
        bottomBar = {
            product?.let { p ->
                BottomActionBar(
                    isWishlisted = isWishlisted,
                    onWish = {
                        if (isWishlisted) {
                            wishlistRepo.removeFromWishlist(p.id) { success ->
                                if (success) isWishlisted = false
                            }
                        } else {
                            wishlistRepo.addToWishlist(p) { success ->
                                if (success) isWishlisted = true
                            }
                        }
                    },
                    onBuy = {
                        val url = p.affiliateLink
                        if (url.isNotBlank()) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                                setPackage("com.amazon.mShop.android.shopping")
                            }

                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                )
                            }
                        }
                    }


                )
            }
        },
        containerColor = Color(0xFFF9F8F6)
    )
    { padding ->

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = Color.Black) }
            }

            product == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) { Text("Product not found", color = Color.Black) }
            }

            else -> {
                val p = product!!
                val pagerState = rememberPagerState { p.images.size }

                // ---------- AUTO-SCROLL EVERY 3 SECONDS ----------
                LaunchedEffect(Unit) {
                    while (true) {
                        delay(3000)
                        val next = (pagerState.currentPage + 1) % p.images.size
                        pagerState.animateScrollToPage(next)
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                        .padding(16.dp)      // <--- Padding everywhere
                ) {

                    // ---------- IMAGE SLIDER ----------
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                    ) { index ->
                        AsyncImage(
                            model = p.images[index],
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // ---------- DOT INDICATORS ----------
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(p.images.size) { index ->
                            val isSelected = pagerState.currentPage == index
                            Box(
                                modifier = Modifier
                                    .size(if (isSelected) 10.dp else 7.dp)
                                    .padding(3.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) Color.Black else Color.Gray)
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ---------- NAME ----------
                    Text(
                        text = p.name,
                        fontSize = 22.sp,
                        color = Color.Black
                    )

                    Spacer(Modifier.height(8.dp))

                    // ---------- PRICE + RATING ----------
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "₹${p.price}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Column(horizontalAlignment = Alignment.End) {
                            Text("⭐ 4.6", fontSize = 16.sp, color = Color.Black)
                            Text("(1,248 reviews)", fontSize = 13.sp, color = Color.Gray)
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ---------- DESCRIPTION ----------
                    p.description?.let {
                        Text(
                            text = it,
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            color = Color.Black
                        )
                    }

                    Spacer(Modifier.height(80.dp))
                }
            }
        }
    }
}



// =============================================================
// FIXED BOTTOM BAR — NOT SCROLLABLE
// =============================================================
@Composable
fun BottomActionBar(
    isWishlisted: Boolean,
    onWish: () -> Unit,
    onBuy: () -> Unit
) {

    val bgColor by animateColorAsState(
        targetValue = if (isWishlisted) Color.Red else Color(0xFFF2F2F2),
        label = ""
    )

    val textColor by animateColorAsState(
        targetValue = if (isWishlisted) Color.White else Color.Black,
        label = ""
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Button(
            onClick = onWish,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 3.dp,
                pressedElevation = 1.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = bgColor,
                contentColor = textColor
            )

        ) {
            Icon(
                painter = painterResource(
                    if (isWishlisted) R.drawable.heart else R.drawable.icons8_heart_50
                ),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = if (isWishlisted) "Wishlisted" else "Add to Wishlist",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }


        Spacer(Modifier.width(12.dp))

        Button(
            onClick = onBuy,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_currency_rupee_24),
                contentDescription = null,
                tint = Color.White
            )
            Spacer(Modifier.width(6.dp))
            Text("Buy Now", color = Color.White)
        }
    }
}
