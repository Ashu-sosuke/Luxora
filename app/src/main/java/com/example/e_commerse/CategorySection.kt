package com.example.e_commerse

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

// ✅ Make sure you import your app's R
import com.example.e_commerse.R
import com.google.accompanist.flowlayout.FlowRow


// 🎨 Theme colors (match HomeScreen)
val MatteBlack = Color(0xFF121212)
val LightBlue = Color(0xFFC2E2FA)


// 📦 Category data model
data class CategoryItem(val icon: Int, val label: String)

// 🗂 Sample category items
val categoryItems = listOf(
    CategoryItem(R.drawable._685b9c27ba225acff3ced2623fab9e4_removebg_preview, "Fashion"),
    CategoryItem(R.drawable._efc229db38a2d72003d91fdfb1588ee_removebg_preview, "Sports"),
    CategoryItem(R.drawable.e27d9a79c1b102515bda65faaddeaf08_removebg_preview, "Electronics"),
    CategoryItem(R.drawable._e0962fc5af2d4c3410444ca1eece857_removebg_preview, "Furniture"),
    CategoryItem(R.drawable.ac74179f9e137790bf16a957e141daa5_removebg_preview, "Food & HealthCare"),
    CategoryItem(R.drawable.d538d17b7b6ea2130ceb006c4e117ed8_removebg_preview, "Smart Gadgets"),
    CategoryItem(R.drawable.d0f95dc10488e627b0d30c650fd81766_removebg_preview, "Kitchen")
)

// 🔹 Composable for a single category item


@Composable
fun CategoryItemView(item: CategoryItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
            color = TextBlack,
            textAlign = TextAlign.Center,
            maxLines = 2,
            softWrap = true
        )
    }
}

// 🔹 Navigation helper
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