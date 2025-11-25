package com.example.e_commerse.recently_viewed

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.e_commerse.Product
import com.example.e_commerse.NeonBlue
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun RVScreen(
    navController: NavController,
    viewmodel: RvViewmodel = viewModel(
        factory = RVViewModelFactory(
            RVRepo(
                ProductDatabase.getDatabase(LocalContext.current).productDao()
            )
        )
    )
) {
    LaunchedEffect(Unit) {
        viewmodel.loadRecentlyVisited()
    }

    val recentProducts = viewmodel.recentlyVisited

    Text(
        text = "Top Picks (Recently Viewed)",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = NeonBlue,
        modifier = Modifier.padding(16.dp)
    )

    if (recentProducts.isEmpty()) {
        Text("No recent items", color = Color.Gray, modifier = Modifier.padding(16.dp))
    } else {
        LazyRow(content = {
            items(recentProducts) { product ->
                RvProductCard(
                    product = product,
                    onClick = {
                        navController.navigate("product_detail/${Uri.encode(product.id)}")
                    }
                )
            }
        })
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun RvProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .width(160.dp)
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {

            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = product.name,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "₹${product.price}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = NeonBlue
            )
        }
    }
}