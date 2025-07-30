package com.example.e_commerse.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.e_commerse.NeonGreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUploadScreen() {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    val categories = listOf("Shirt", "Shoe", "Electronic", "Watch", "Furniture")

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imgUrl by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var uploadStatus by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var isAdmin by remember { mutableStateOf<Boolean?>(null) } // null = loading

    // Fetch role from Firestore
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { uid ->
            db.collection("admins").document(uid).get()
                .addOnSuccessListener { doc ->
                    isAdmin = doc.getString("role") == "admin"
                }
                .addOnFailureListener {
                    isAdmin = false
                }
        } ?: run {
            isAdmin = false
        }
    }

    when (isAdmin) {
        null -> {
            // Loading
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        false -> {
            // Not an admin
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("🚫 Access Denied", color = Color.Red, fontSize = 18.sp)
            }
        }
        true -> {
            // Admin UI
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Upload Product",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonGreen
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Select Category") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    category = selectionOption
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = imgUrl,
                    onValueChange = { imgUrl = it },
                    label = { Text("Image URL") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (imgUrl.isNotBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imgUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Image Preview",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }

                Button(
                    onClick = {
                        if (name.isNotBlank() && price.isNotBlank() && description.isNotBlank()
                            && imgUrl.isNotBlank() && category.isNotBlank()
                        ) {
                            val product = hashMapOf(
                                "name" to name,
                                "price" to price,
                                "description" to description,
                                "imgUrl" to imgUrl,
                                "category" to category,
                                "timestamp" to System.currentTimeMillis()
                            )

                            db.collection("products").add(product)
                                .addOnSuccessListener {
                                    uploadStatus = "✅ Product uploaded successfully!"
                                    name = ""
                                    price = ""
                                    description = ""
                                    imgUrl = ""
                                    category = ""
                                }
                                .addOnFailureListener {
                                    uploadStatus = "❌ Failed: ${it.message}"
                                }
                        } else {
                            uploadStatus = "⚠️ Please fill all fields."
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Upload Product")
                }

                if (uploadStatus.isNotBlank()) {
                    Text(uploadStatus, color = Color.White, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}
