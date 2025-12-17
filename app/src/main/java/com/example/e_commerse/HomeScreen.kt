package com.example.e_commerse

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.e_commerse.recently_viewed.ProductDatabase
import com.example.e_commerse.recently_viewed.RVRepo
import com.example.e_commerse.recently_viewed.RVScreen
import com.example.e_commerse.recently_viewed.RVViewModelFactory
import com.example.e_commerse.recently_viewed.RvViewmodel
import com.example.e_commerse.sub_selection.ProductCard
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val MatteWhite = Color(0xFFFFFFFF)
val NeonBlue = Color(0xFF0095FF)
val LightBlueGradient = Color(0xFFE3F2FD)
val SkyBlue = Color(0xFFB3E5FC)
val SoftGray = Color(0xFFF9F9F9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    val systemController = rememberSystemUiController()

    // Transparent system bars
    SideEffect {
        systemController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    val context = LocalContext.current
    val db = remember { ProductDatabase.getDatabase(context) }
    val repo = remember { RVRepo(db.productDao()) }
    val rvViewmodel: RvViewmodel = viewModel(factory = RVViewModelFactory(repo))

    // Load recently viewed once
    LaunchedEffect(Unit) {
        rvViewmodel.loadRecentlyVisited()
    }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            BottomNavBar(
                navController = navController,
                currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route,
                bottomItems = listOf(
                    Screen.HomeScreen,
                    Screen.ExploreScreen,
                    Screen.WishlistScreen,
                    Screen.ProfileScreen
                ),
                bottomIcons = listOf(
                    R.drawable.outline_home_24,
                    R.drawable.expolre,
                    R.drawable.icons8_heart_50,
                    R.drawable.outline_person_4_24
                ),
                backgroundColor = Color(0xFFFFF2D7)
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ) {
            HomeContent(
                navController = navController,
                rvViewmodel = rvViewmodel
            )
        }
    }
}

@Composable
fun HomeContent(
    navController: NavController,
    rvViewmodel: RvViewmodel
) {

    val searchViewModel: SearchViewModel = viewModel()

    var searchQuery by remember { mutableStateOf("") }
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }

    LaunchedEffect(searchQuery) {
        searchViewModel.searchProducts(searchQuery) {
            products = it
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        SearchBar(
            searchQuery = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        Spacer(Modifier.height(8.dp))

        if (searchQuery.isNotBlank()) {
            Column {
                products.forEach { product ->
                    Text(
                        text = product.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("product_detail/${product.id}")
                            }
                            .padding(vertical = 12.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Divider(color = Color.LightGray)
                }
            }
        }
        else {

            // 🏠 HOME CONTENT
            ImageSlider()

            Spacer(Modifier.height(12.dp))

            CategoriesSection(categoryItems, navController)

            Spacer(Modifier.height(16.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFB08968).copy(alpha = 0.5f)
            )

            Spacer(Modifier.height(16.dp))

            RVScreen(navController, rvViewmodel)

            Spacer(Modifier.height(80.dp))
        }
    }

}

@Composable
fun SearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit
) {



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onQueryChange,
            placeholder = { Text("Search Products....", color = Color.Gray) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = NeonBlue)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("")}) {
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


