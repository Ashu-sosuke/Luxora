package com.example.e_commerse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

data class TopProduct(val imgUrl: String, val name: String, val price: String)

val topPicksList = listOf(
    TopProduct("https://images.pexels.com/photos/298863/pexels-photo-298863.jpeg", "Kitchen", "$30"),
    TopProduct("https://images.pexels.com/photos/298863/pexels-photo-298863.jpeg", "Electronics", "$250"),
    TopProduct("https://images.pexels.com/photos/298863/pexels-photo-298863.jpeg", "Fashion", "$99"),
    TopProduct("https://images.pexels.com/photos/298863/pexels-photo-298863.jpeg", "Grocery", "$15"),
    TopProduct("https://images.pexels.com/photos/298863/pexels-photo-298863.jpeg", "Beauty", "$40"),
)

@Composable
fun TopPicksGrid(onProductClick: (TopProduct) -> Unit = {}) {
    val gridHeight = if (topPicksList.size <= 4) 400.dp else 600.dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(gridHeight)
            .padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(topPicksList, key = { it.name }) { product ->
            ProductCard(product, onProductClick)
        }
    }
}


@Composable
fun ProductCard(product: TopProduct, onClick: (TopProduct) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f)
            .clickable { onClick(product) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imgUrl)
                    .crossfade(true)
                    .size(512)
                    .build(),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = product.price,
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}

