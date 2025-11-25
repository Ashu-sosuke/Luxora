package com.example.e_commerse

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
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

val MatteBlack = Color(0xFF121212)
val LightBlue = Color(0xFFC2E2FA)

data class CategoryItem(val icon: Int, val label: String)

// 🗂 Category items list
val categoryItems = listOf(
    CategoryItem(R.drawable._685b9c27ba225acff3ced2623fab9e4_removebg_preview, "Men's Fashion"),
    CategoryItem(R.drawable._f17c16d33afa0c81bab539eb63a6e7d_removebg_preview, "Women's Fashion"),
    CategoryItem(R.drawable._efc229db38a2d72003d91fdfb1588ee_removebg_preview, "Sports"),
    CategoryItem(R.drawable.e27d9a79c1b102515bda65faaddeaf08_removebg_preview, "Electronics"),
    CategoryItem(R.drawable._e0962fc5af2d4c3410444ca1eece857_removebg_preview, "Furniture"),
    CategoryItem(R.drawable.ac74179f9e137790bf16a957e141daa5_removebg_preview, "Food & HealthCare"),
    CategoryItem(R.drawable.d538d17b7b6ea2130ceb006c4e117ed8_removebg_preview, "Smart Gadgets"),
    CategoryItem(R.drawable.d0f95dc10488e627b0d30c650fd81766_removebg_preview, "Kitchen")
)


// 🔹 Category Card (Main item)
@Composable
fun CategoryItemView(item: CategoryItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(110.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(LightBlue)
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.label,
            modifier = Modifier.size(70.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.label,
            fontSize = 14.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}


fun navigateToCategory(navController: NavController, label: String) {
    when (label) {
        "Men's Fashion" -> navController.navigate(Screen.MenFashionScreen.route)
        "Women's Fashion" -> navController.navigate(Screen.WomenFashionScreen.route)
        "Sports" -> navController.navigate(Screen.SportScreen.route)
        "Electronics" -> navController.navigate(Screen.ElectronicScreen.route)
        "Furniture" -> navController.navigate(Screen.FurnitureScreen.route)
        "Food & HealthCare" -> navController.navigate(Screen.FoodScreen.route)
        "Smart Gadgets" -> navController.navigate(Screen.SmartDevice.route)
        "Kitchen" -> navController.navigate(Screen.KitchenScreen.route)
    }
}


// 🔹 Category Section (LazyRow + More dropdown)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesSection(
    categoryItems: List<CategoryItem>,
    navController: NavController
) {
    val maxVisible = 2
    val visibleItems = categoryItems.take(maxVisible)
    val overflowItems = categoryItems.drop(maxVisible)

    var showSheet by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = "Categories",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = NeonBlue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(visibleItems) { item ->
                CategoryItemView(item) {
                    navigateToCategory(navController, item.label)
                }
            }

            item {
                CategoryMoreButton { showSheet = true }
            }
        }

        // 🔹 Bottom Sheet
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                modifier = Modifier.fillMaxHeight(0.65f)
            ) {
                Text(
                    text = "More Categories",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(overflowItems) { item ->
                        CategoryItemView(item) {
                            showSheet = false
                            navigateToCategory(navController, item.label)
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun CategoryMoreButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE7F1FF))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "More >>",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0066CC)
        )
    }
}

