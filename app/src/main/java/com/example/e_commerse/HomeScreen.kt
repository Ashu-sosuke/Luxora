package com.example.e_commerse

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.flowlayout.FlowRow
import kotlin.math.sin

val MatteWhite = Color(0xFFFFFFFF)
val NeonBlue = Color(0xFF0095FF)
val TextBlack = Color(0xFF1A1A1A)
val LightGray = Color(0xFFF5F5F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, modifier: Modifier = Modifier) {


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

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                color = Color(0xFFB4D4FF)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Luxora",
                        color = TextBlack,
                        modifier = Modifier.padding(top = 20.dp),
                        fontWeight = FontWeight.Black,
                        fontSize = 26.sp,
                        fontFamily = FontFamily.Serif
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = MatteWhite, tonalElevation = 4.dp) {
                bottomItems.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = bottomIcons[index]),
                                contentDescription = screen.route,
                                tint = if (currentRoute == screen.route) NeonBlue else Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                screen.route.substringBefore("/").replaceFirstChar { it.uppercase() },
                                color = if (currentRoute == screen.route) NeonBlue else Color.Gray
                            )
                        },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.HomeScreen.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        },
        containerColor = MatteWhite
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MatteWhite)
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
            .background(MatteWhite)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Search Bar
        SearchBar()

        // 🖼 Image Slider
        ImageSlider()

        // 🏷 Categories Title
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

        // 🗂 Categories Grid using FlowRow
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

        // 🛒 Top Picks Grid
        TopPicksGrid()

        Spacer(modifier = Modifier.height(80.dp)) // bottom bar space
    }
}

@Composable
fun SearchBar(){
    var seachQuery by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = seachQuery,
            onValueChange = {seachQuery = it},
            placeholder = {Text("Search Products....", color = Color.Gray)},
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = NeonBlue
                )
            },
            trailingIcon = {
                if (seachQuery.isNotEmpty()){
                    IconButton(onClick = {seachQuery = ""}) {
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
                focusedLabelColor = NeonBlue
            ),
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = { /*TODO Add MIC */},
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
            ) {
            Icon(
                painter = painterResource(R.drawable.baseline_mic_24),
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}