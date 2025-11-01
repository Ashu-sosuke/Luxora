package com.example.e_commerse

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



data class ProductItem(
    val id: String = "",
    val title: String = "",
    val descriptionPoints: List<String> = emptyList(),
    val price: Double = 0.0,
    val category: String = "",
    val rating: Double = 0.0,
    val imageUrls: List<String> = emptyList()
)



class ProductViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _topPicks = MutableStateFlow<List<ProductItem>>(emptyList())
    val topPicks: StateFlow<List<ProductItem>> = _topPicks

    fun fetchTopPicks() {
        viewModelScope.launch {
            db.collection("products")
                .limit(10) // fetch limited top picks
                .addSnapshotListener { snapshot, e ->
                    if (e != null) return@addSnapshotListener
                    _topPicks.value = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(ProductItem::class.java)?.copy(id = doc.id)
                    } ?: emptyList()
                }
        }
    }

}


@Composable
fun ProductItemCard(item: ProductItem) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
            .clickable { /* navigate to product details */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(Color.White)
        ) {
            AsyncImage(
                model = item.imageUrls.firstOrNull(),
                contentDescription = item.title,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "₹${item.price}",
                fontSize = 13.sp,
                color = NeonBlue,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ProductListScreen(viewModel: ProductViewModel = viewModel()) {
    val products by viewModel.topPicks.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchTopPicks()
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products) { product ->
            ProductItemCard(product)
        }
    }
}

