package com.example.e_commerse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
        R.drawable.outline_home_24,
        R.drawable.expolre,
        R.drawable.outline_shopping_cart_24,
        R.drawable.icons8_heart_50,
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
        containerColor = MatteWhite,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Your Order",
                        fontWeight = FontWeight.Bold,
                        color = NeonBlue,
                        fontSize = 22.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
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
    ){innerpadding->
        Column(
            modifier = Modifier.padding(innerpadding)
        ) {  }

    }
}