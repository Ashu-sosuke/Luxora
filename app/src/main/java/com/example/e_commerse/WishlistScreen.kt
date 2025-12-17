package com.example.e_commerse

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.e_commerse.sub_selection.ProductCard
import com.google.firebase.storage.FirebaseStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(navController: NavController) {


    val bottomItems = listOf(
        Screen.HomeScreen,
        Screen.ExploreScreen,
        Screen.WishlistScreen,
        Screen.ProfileScreen
    )

    val bottomIcons = listOf(
        R.drawable.baseline_home_24,
        R.drawable.expolre,
        R.drawable.icons8_heart_50,
        R.drawable.outline_person_4_24
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val viewModel: WishlistViewModel = viewModel()
    var wishlist by remember { mutableStateOf<List<Product>>(emptyList()) }

    LaunchedEffect(Unit) {
        viewModel.getWishlist { list ->
            wishlist = list
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Wishlist", fontSize = 22.sp, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
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
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {

                if (wishlist.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Your wishlist is empty.", color = Color.Gray)
                    }
                } else {
                    wishlist.forEach { product ->
                        WishlistItemCard(
                            product = product,
                            onClick = {
                                navController.navigate("product_detail/${product.id}")
                            },
                            onRemove = {
                                viewModel.removeFromWishlist(product.id)
                            },
                            onBuyNow = {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(product.affiliateLink)
                                )
                                navController.context.startActivity(intent)
                            }
                        )

                        Spacer(Modifier.height(16.dp))
                    }

                }
            }
        }
    }
}


@Composable
fun WishlistItemCard(
    product: Product,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    onBuyNow: () -> Unit
) {
    val imageUrl = product.images.firstOrNull()

    Log.d("WISHLIST_IMAGE", product.images.firstOrNull() ?: "NO IMAGE")


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(90.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(14.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // ✅ TEXT + BUTTONS
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    Text(
                        text = product.name,
                        fontSize = 15.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "₹${product.price}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7B542F)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    OutlinedButton(
                        onClick = onRemove,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Remove", fontSize = 12.sp, color = Color.Black)
                    }

                    Button(
                        onClick = onBuyNow,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF854836)),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Text("Buy Now", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }
    }
}
