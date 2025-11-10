package com.example.e_commerse

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(navController: NavController) {
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
    val currentRoute by navController.currentBackStackEntryAsState()

    val systemController = rememberSystemUiController()

    SideEffect {
        systemController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(
                    "Explore ",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                    )},
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.ExploreScreen.route)
                    }) { Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.Black
                    )}
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightBlueGradient
                )
            )
        },
        containerColor = Color.Transparent,
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .shadow(12.dp, RoundedCornerShape(50))
                .background(Color(0xFFF9F8F6), shape = RoundedCornerShape(50))
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            bottomItems.forEachIndexed { index, screen ->
                val isSelected = currentRoute == screen.route

                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.2f else 1f,
                    animationSpec = tween(durationMillis = 200),
                    label = "iconScale"
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) Color.LightGray else Color.Transparent
                        )
                        .clickable {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.HomeScreen.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = bottomIcons[index]),
                        contentDescription = screen.route,
                        tint = if (isSelected) Color.Black else Color.Black,
                        modifier = Modifier
                            .size(22.dp)
                            .graphicsLayer(scaleX = scale, scaleY = scale)
                    )
                }
            }
        }
    }
}



@Composable
fun ECategoryItemView(item: CategoryItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(LightBlue)
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.label,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = item.label,
            fontSize = 14.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center,
            maxLines = 2,
            softWrap = true
        )
    }
}

