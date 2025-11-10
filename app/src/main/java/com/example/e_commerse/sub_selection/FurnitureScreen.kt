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

data class Furniture(val imgUrl: String, val name: String)

val furnitureList = listOf(
    Furniture("https://i.pinimg.com/1200x/a2/c4/c9/a2c4c9e1c609eb403b2ebe9bdc20b57f.jpg", "Sofa"),
    Furniture("https://i.pinimg.com/736x/b0/c5/59/b0c559b4a50a4c2f67ee06d6c9c1d1a7.jpg", "Bean Bag"),
    Furniture("https://i.pinimg.com/736x/bd/bc/a9/bdbca905d1f3b3895cc9570c8c70e9e2.jpg", "Chair"),
    Furniture("https://i.pinimg.com/736x/87/e1/9e/87e19e4abf5dac13e54dc3b394ceb33d.jpg", "Dining Table")
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FurnitureScreen(navController: NavController) {
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
                        "Furniture",
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.HomeScreen.route) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Gray
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
            FurnitureGridWithTitle("Furniture", furnitureList){ item ->
            }
        }
    }
}

@Composable
fun FurnitureGridWithTitle(
    title: String,
    list: List<Furniture>,
    onItemClick: (Furniture) -> Unit = {}
) {
    Column {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = NeonGreen,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
        )

        val chunked = list.chunked(2)
        chunked.forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                for (item in rowItems) {
                    FurnitureItemCard(item = item, onClick = onItemClick, modifier = Modifier.weight(1f))
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun FurnitureItemCard(item: Furniture, onClick: (Furniture) -> Unit, modifier: Modifier = Modifier) {
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
