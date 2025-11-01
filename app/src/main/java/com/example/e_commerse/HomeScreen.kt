package com.example.e_commerse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val MatteWhite = Color(0xFFFFFFFF)
val NeonBlue = Color(0xFF0095FF)
val LightBlueGradient = Color(0xFFE3F2FD)
val SkyBlue = Color(0xFFB3E5FC)
val SoftGray = Color(0xFFF9F9F9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, modifier: Modifier = Modifier) {

    val systemController = rememberSystemUiController()

    // Make system bars transparent
    SideEffect {
        systemController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    val bottomItems = listOf(
        Screen.HomeScreen,
        Screen.ExploreScreen,
        Screen.OrderScreen,
        Screen.WishlistScreen,
        Screen.ProfileScreen
    )

    val bottomIcons = listOf(
        R.drawable.outline_home_24,
        R.drawable.expolre,
        R.drawable.outline_shopping_cart_24,
        R.drawable.icons8_heart_50,
        R.drawable.outline_person_4_24
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            BottomNavBar(
                navController = navController,
                currentRoute = currentRoute,
                bottomItems = bottomItems,
                bottomIcons = bottomIcons
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    // ✨ Beautiful vertical gradient
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE3F2FD), // soft light blue top
                            Color(0xFFFFFFFF), // white middle
                            Color(0xFFB3E5FC)  // gentle blue bottom
                        )
                    )
                )
                .padding(innerPadding)
        ) {
            HomeContent(navController)
        }
    }
}

// 🔹 Home content with 3-column category grid
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeContent(navController: NavController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🔍 Search Bar

            SearchBar()

            // 🖼 Image Slider
            ImageSlider()

        // 🏷 Categories
        Text(
            text = "Categories",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = NeonBlue,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)
        )

        FlowRow(
            mainAxisSpacing = 16.dp,
            crossAxisSpacing = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            categoryItems.forEach { item ->
                Box(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .width(100.dp)
                ) {
                    CategoryItemView(item) {
                        navigateToCategory(navController, item.label)
                    }
                }
            }
        }

        // 🛒 Top Picks
        Text(
            text = "Top Picks",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = NeonBlue,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)
        )

        ProductListScreen()
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun SearchBar() {
    var searchQuery by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search Products....", color = Color.Gray) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = NeonBlue)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = NeonBlue)
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonBlue,
                unfocusedBorderColor = Color.LightGray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = NeonBlue,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedLabelColor = NeonBlue
            ),
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = { /*TODO: Add MIC*/ },
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFFFEDF3), shape = RoundedCornerShape(16.dp))
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_mic_24),
                contentDescription = null,
                tint = NeonBlue
            )
        }
    }
}
