package com.example.e_commerse.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.e_commerse.MatteBlack
import com.example.e_commerse.NeonBlue
import com.example.e_commerse.R
import com.example.e_commerse.Screen
import com.example.e_commerse.login.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUploadScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    val mainCategories = listOf("Fashion", "Electronics", "Furniture","Sports", "Food & HealthCare","Smart Gadgets","Kitchen")
    val fashionSubCategories = listOf("Shirt", "T-Shirt", "Jeans", "Kurta-Pyjama","Saree","Kurti","Tank Top","Maxi")
    val electronicSubCategories = listOf("Mobile", "Laptop", "Mouse","Keyboard")
    val furnitureSubCategories = listOf("Sofa", "Chair", "Bean Bag", "Dinning Table")
    val sportsSubCategory = listOf("Cycle","Yoga Mat", "Dumbbell", "Cricket")
    val foodSubCategory = listOf("Penut Butter", "Chocolate","Coffee","Dry Fruit")
    val smartDeviceSubCategory = listOf("Smart Watch","Smart Glasses", "Smart brush","Smart bottle")
    val kitchenSubCategory = listOf("Refrigerator","Stove","Masala Set")

    var mainCategory by remember { mutableStateOf("") }
    var subCategory by remember { mutableStateOf("") }
    var subOptions by remember { mutableStateOf(listOf<String>()) }

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imgUrl by remember { mutableStateOf("") }
    var uploadStatus by remember { mutableStateOf("") }

    var expandedMain by remember { mutableStateOf(false) }
    var expandedSub by remember { mutableStateOf(false) }

    val viewModel: AuthViewModel = viewModel()
    var isAdmin by remember { mutableStateOf<Boolean?>(null) }

    // Check admin status
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { uid ->
            db.collection("admins").document(uid).get()
                .addOnSuccessListener { doc -> isAdmin = doc.getString("role") == "admin" }
                .addOnFailureListener { isAdmin = false }
        } ?: run { isAdmin = false }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.shop),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Luxora",
                            color = NeonBlue,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 29.sp
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MatteBlack)
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            when (isAdmin) {
                null -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                false -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🚫 Access Denied", color = Color.Red, fontSize = 18.sp)
                }

                true -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .background(MatteBlack.copy(alpha = 0.85f))
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Upload Product", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = NeonBlue)

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Product Name", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.LightGray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Price", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.LightGray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description", color = Color.Gray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.LightGray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedMain,
                        onExpandedChange = { expandedMain = !expandedMain }
                    ) {
                        TextField(
                            value = mainCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Main Category", color = Color.Gray) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMain) },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedMain,
                            onDismissRequest = { expandedMain = false }
                        ) {
                            mainCategories.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        mainCategory = option
                                        subCategory = ""
                                        subOptions = when (option) {
                                            "Fashion" -> fashionSubCategories
                                            "Electronics" -> electronicSubCategories
                                            "Furniture" -> furnitureSubCategories
                                            "Sports" -> sportsSubCategory
                                            "Food & HealthCare" -> foodSubCategory
                                            "Smart Gadgets" -> smartDeviceSubCategory
                                            "Kitchen" -> kitchenSubCategory
                                            else -> emptyList()
                                        }
                                        expandedMain = false
                                    }
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedSub,
                        onExpandedChange = { expandedSub = !expandedSub }
                    ) {
                        TextField(
                            value = subCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Sub Category", color = Color.Gray) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSub) },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedSub,
                            onDismissRequest = { expandedSub = false }
                        ) {
                            subOptions.forEach { sub ->
                                DropdownMenuItem(
                                    text = { Text(sub) },
                                    onClick = {
                                        subCategory = sub
                                        expandedSub = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = imgUrl,
                        onValueChange = { imgUrl = it },
                        label = { Text("Image URL", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.LightGray
                        ),
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
                            val priceValue = price.trim().replace(",", "").toDoubleOrNull()
                            if (name.isNotBlank() && priceValue != null && description.isNotBlank()
                                && imgUrl.isNotBlank() && mainCategory.isNotBlank() && subCategory.isNotBlank()
                            ) {
                                val product = hashMapOf(
                                    "name" to name,
                                    "price" to priceValue,
                                    "description" to description,
                                    "imgUrl" to imgUrl,
                                    "mainCategory" to mainCategory,
                                    "subCategory" to subCategory,
                                    "timestamp" to System.currentTimeMillis()
                                )

                                db.collection("products").add(product)
                                    .addOnSuccessListener {
                                        uploadStatus = "✅ Product uploaded successfully!"
                                        name = ""
                                        price = ""
                                        description = ""
                                        imgUrl = ""
                                        mainCategory = ""
                                        subCategory = ""
                                    }
                                    .addOnFailureListener {
                                        uploadStatus = "❌ Upload failed: ${it.message}"
                                    }
                            } else {
                                uploadStatus = "⚠️ Please fill all fields correctly."
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
                    ) {
                        Text("Upload Product", color = Color.White)
                    }

                    Button(
                        onClick = {
                            viewModel.logout(context) {
                                navController.navigate(Screen.LoginScreen.route) {
                                    popUpTo(Screen.LoginScreen.route) { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonBlue,
                            contentColor = MatteBlack
                        )
                    ) {
                        Text("Logout", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    if (uploadStatus.isNotBlank()) {
                        Text(uploadStatus, color = Color.White, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
        }
    }
}
