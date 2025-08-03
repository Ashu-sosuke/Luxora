package com.example.e_commerse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavController){
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
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route


    Scaffold(
        containerColor = MatteBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Your Order",
                        fontWeight = FontWeight.Bold,
                        color = NeonGreen,
                        fontSize = 22.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        bottomBar = {
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
    ){innerpadding->
        Column(
            modifier = Modifier.padding(innerpadding)
        ) {  }

    }
}