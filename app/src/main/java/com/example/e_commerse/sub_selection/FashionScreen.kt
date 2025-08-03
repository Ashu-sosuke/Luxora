package com.example.e_commerse.sub_selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.e_commerse.R
import com.example.e_commerse.Screen
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
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val bottomItems = listOf(
        Screen.HomeScreen,
        Screen.ExploreScreen,
        Screen.OrderScreen,
        Screen.WishlistScreen,
        Screen.ProfileScreen
    )

    val bottomIcons = listOf(
        R.drawable.baseline_home_24,
        R.drawable.expolre,
        R.drawable.outline_shopping_cart_24,
        R.drawable.heart,
        R.drawable.outline_person_4_24
    )

    Scaffold(
        containerColor = MatteBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Fashion",
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.DarkGray
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.Black) {
                bottomItems.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = bottomIcons[index]),
                                contentDescription = screen.route,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(screen.route.substringBefore("/").replaceFirstChar { it.uppercase() })
                        },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.HomeScreen.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonGreen,
                            selectedTextColor = NeonGreen,
                            indicatorColor = Color.DarkGray,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(MatteBlack)
                .padding(12.dp)
        ) {
            ItemGridWithTitle("Men's Collection", MensCothes) { item ->
                navController.navigate("product_list/Fashion/${encodeParam(item.name)}")
            }
            Spacer(modifier = Modifier.height(32.dp))
            ItemGridWithTitle("Women's Collection", WomensClothes) { item ->
                navController.navigate("product_list/Fashion/${encodeParam(item.name)}")
            }
        }
    }
}


@Composable
fun ItemGridWithTitle(
    title: String,
    clothes: List<cloth>,
    onItemClick: (cloth) -> Unit = {}
) {
    Column {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = NeonGreen,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
        )

        val chunked = clothes.chunked(2)
        chunked.forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                for (item in rowItems) {
                    ItemCard(item = item, onClick = onItemClick, modifier = Modifier.weight(1f))
                }

                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ItemCard(item: cloth, onClick: (cloth) -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp)),
        onClick = { onClick(item) },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1C)),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.imgUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.name,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = NeonGreen,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

fun encodeParam(param: String): String =
    URLEncoder.encode(param, StandardCharsets.UTF_8.toString())
