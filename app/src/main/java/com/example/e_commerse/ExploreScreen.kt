package com.example.e_commerse

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(navController: NavController) {
    val bottomIcons = listOf(
        R.drawable.baseline_home_24,
        R.drawable.expolre,
        R.drawable.outline_shopping_cart_24,
        R.drawable.heart,
        R.drawable.outline_person_4_24
    )
    val bottomItems = listOf(
        Screen.HomeScreen,
        Screen.ExploreScreen,
        Screen.OrderScreen,
        Screen.WishlistScreen,
        Screen.ProfileScreen
    )
    val currentRoute by navController.currentBackStackEntryAsState()

    Scaffold(
        containerColor = MatteBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Explore Category",
                        fontWeight = FontWeight.Bold,
                        color = NeonGreen,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                currentRoute = currentRoute?.destination?.route,
                bottomItems = bottomItems,
                bottomIcons = bottomIcons
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(categoryItems) { item ->
                ECategoryItemView(item = item) {
                    navigateToCategory(navController, item.label)
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(
    navController: NavController,
    currentRoute: String?,
    bottomItems: List<Screen>,
    bottomIcons: List<Int>
) {
    NavigationBar(
        containerColor = Color.Black,
        tonalElevation = 4.dp
    ) {
        bottomItems.forEachIndexed { index, screen ->
            val isSelected = currentRoute == screen.route
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = bottomIcons[index]),
                        contentDescription = screen.route,
                        tint = if (isSelected) NeonGreen else Color.LightGray,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        screen.route.substringBefore("/").replaceFirstChar { it.uppercase() },
                        color = if (isSelected) NeonGreen else Color.LightGray,
                        fontSize = 12.sp
                    )
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.HomeScreen.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                alwaysShowLabel = true
            )
        }
    }
}

@Composable
fun ECategoryItemView(item: CategoryItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.DarkGray)
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.label,
            tint = NeonGreen,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = item.label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


fun navigateToCategory(navController: NavController, label: String) {
    when (label) {
        "Fashion" -> navController.navigate(Screen.FashionScreen.route)
        "Sports" -> navController.navigate(Screen.SportScreen.route)
        "Electronics" -> navController.navigate(Screen.ElectronicScreen.route)
        "Furniture" -> navController.navigate(Screen.FurnitureScreen.route)
        "Food & HealthCare" -> navController.navigate(Screen.FoodScreen.route)
        "Smart Gadgets" -> navController.navigate(Screen.SmartDevice.route)
        "Kitchen" -> navController.navigate(Screen.KitchenScreen.route)
    }
}
