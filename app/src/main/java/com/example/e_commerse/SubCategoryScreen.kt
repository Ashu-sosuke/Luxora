package com.example.e_commerse


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.e_commerse.MatteBlack
import com.example.e_commerse.NeonGreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubCategoryScreen(mainCategory: String, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var subCategories by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(mainCategory) {
        db.collection("products")
            .whereEqualTo("mainCategory", mainCategory)
            .get()
            .addOnSuccessListener { result ->
                val uniqueSubs = result.documents.mapNotNull { it.getString("subCategory") }.distinct()
                subCategories = uniqueSubs
            }
    }

    Scaffold(
        containerColor = MatteBlack,
        topBar = {
            TopAppBar(
                title = { Text(mainCategory, color = NeonGreen) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NeonGreen)
                    }
                }
            )
        }
    ) { padding ->
        if (subCategories.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else {
            LazyColumn(
                contentPadding = padding,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                items(subCategories) { sub ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val encodedMain = URLEncoder.encode(mainCategory, StandardCharsets.UTF_8.toString())
                                val encodedSub = URLEncoder.encode(sub, StandardCharsets.UTF_8.toString())
                                navController.navigate("product_list/$encodedMain/$encodedSub")
                            },
                        colors = CardDefaults.cardColors(containerColor = MatteBlack.copy(alpha = 0.7f))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(sub, color = NeonGreen)
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = NeonGreen)
                        }
                    }
                }
            }
        }
    }
}
