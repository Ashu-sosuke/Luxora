package com.example.e_commerse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.e_commerse.MatteBlack
import com.example.e_commerse.NeonGreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCategoryScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var categories by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(true) {
        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val uniqueCats = result.documents.mapNotNull { it.getString("mainCategory") }.distinct()
                categories = uniqueCats
            }
    }

    Scaffold(
        containerColor = MatteBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("🛍️ Categories", color = NeonGreen, fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        if (categories.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else {
            LazyColumn(
                contentPadding = padding,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                items(categories) { category ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val encoded = URLEncoder.encode(category, StandardCharsets.UTF_8.toString())
                                navController.navigate("sub_category/$encoded")
                            },
                        colors = CardDefaults.cardColors(containerColor = MatteBlack.copy(alpha = 0.7f))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(category, color = NeonGreen, fontWeight = FontWeight.Medium)
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = NeonGreen)
                        }
                    }
                }
            }
        }
    }
}
