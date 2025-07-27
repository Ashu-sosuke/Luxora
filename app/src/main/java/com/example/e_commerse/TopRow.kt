package com.example.e_commerse

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

// --------------------- Category Section ---------------------

data class CategoryItem(val icon: Int, val label: String)

val categoryItems = listOf(
    CategoryItem(R.drawable.icons8_clothes_24, "Fashion"),
    CategoryItem(R.drawable.icons8_sport_24, "Sports"),
    CategoryItem(R.drawable.eletrnic, "Electronics"),
    CategoryItem(R.drawable.furniture, "Furniture"),
    CategoryItem(R.drawable.food, "Food & HealthCare"),
    CategoryItem(R.drawable.smart, "Smart Gadgets"),
    CategoryItem(R.drawable.kitchenicon, "Kitchen")
)

@Composable
fun CategoryRow(navController: NavController) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        items(categoryItems) { item ->
            CategoryItemView(item = item, onClick = {
                if (item.label == "Fashion") {
                    navController.navigate(Screen.FashionScreen.route)
                }
                if(item.label == "Sports") {
                    navController.navigate(Screen.SportScreen.route)
                }
                if(item.label == "Electronics") {
                    navController.navigate(Screen.ElectronicScreen.route)
                }
                if(item.label == "Furniture") {
                    navController.navigate(Screen.FurnitureScreen.route)
                }
                if(item.label == "Food & HealthCare") {
                    navController.navigate(Screen.FoodScreen.route)
                }
                if(item.label == "Smart Gadgets") {
                    navController.navigate(Screen.SmartDevice.route)
                }
                if(item.label == "Kitchen") {
                    navController.navigate(Screen.KitchenScreen.route)
                }
            })
        }
    }
}

@Composable
fun CategoryItemView(item: CategoryItem, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Surface(
            shape = CircleShape,
            border = BorderStroke(1.dp, Color.Gray),
            modifier = Modifier.size(64.dp),
            color = Color.White,
            tonalElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(item.icon),
                    contentDescription = item.label,
                    tint = Color.DarkGray,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = item.label,
            fontSize = 12.sp,
            color = Color.DarkGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// --------------------- Image Slider ---------------------

val imageList = listOf(
    R.drawable.kitchen,
    R.drawable.electronics,
    R.drawable.fashion
)

@Composable
fun ImageSlider() {
    val pagerState = rememberPagerState(pageCount = { imageList.size })
    var animateAlpha by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(pagerState.currentPage) {
        while (true) {
            delay(3000)
            animateAlpha = 0f
            delay(250)
            pagerState.scrollToPage((pagerState.currentPage + 1) % imageList.size)
            animateAlpha = 1f
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
        ) { page ->
            Image(
                painter = painterResource(imageList[page]),
                contentDescription = "Image $page",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = animateAlpha }
            )
        }

        DotIndicator(
            totalDots = imageList.size,
            selectedIndex = pagerState.currentPage,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun DotIndicator(
    totalDots: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    selectedColor: Color = Color.DarkGray,
    unSelectedColor: Color = Color.LightGray
) {
    Row(
        modifier = modifier
            .padding(top = 8.dp)
            .wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == selectedIndex) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(if (index == selectedIndex) selectedColor else unSelectedColor)
            )
        }
    }
}
