package com.example.e_commerse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.e_commerse.login.AuthViewModel

val NeonGreen = Color(0xFF00FF9C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = AuthViewModel() // for preview safety
) {
    val context = LocalContext.current

    // Fetch user data once
    LaunchedEffect(Unit) {
        authViewModel.fetchUserData()
    }

    val firstName by authViewModel.userFirstName
    val lastName by authViewModel.userLastName
    val email by authViewModel.userEmail
    val address by authViewModel.userAddress

    val bottomItems = listOf(
        Screen.HomeScreen,
        Screen.ExploreScreen,
        Screen.OrderScreen,
        Screen.WishlistScreen,
        Screen.ProfileScreen
    )

    val bottomIcons = listOf(
        R.drawable.baseline_home_24,
        R.drawable.expolre,
        R.drawable.outline_shopping_cart_24,
        R.drawable.icons8_heart_50,
        R.drawable.outline_person_4_24
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        containerColor = MatteWhite,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        color = NeonBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MatteBlack
                )
            )
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                currentRoute = currentRoute,
                bottomItems = bottomItems,
                bottomIcons = bottomIcons,
            )
        }
    ) { innerPadding ->
        ProfileContent(
            modifier = Modifier.padding(innerPadding),
            firstName = firstName,
            lastName = lastName,
            email = email,
            address = address,
            authViewModel = authViewModel,
            navController = navController
        )
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    firstName: String,
    lastName: String,
    email: String,
    address: String,
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        ProfileCard(icon = Icons.Default.Person, label = "First Name", value = firstName)
        ProfileCard(icon = Icons.Default.Person, label = "Last Name", value = lastName)
        ProfileCard(icon = Icons.Default.Email, label = "Email", value = email)
        ProfileCard(icon = Icons.Default.LocationOn, label = "Address", value = address)

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                authViewModel.logout(context) {
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.ProfileScreen.route) { inclusive = true }
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonGreen,
                contentColor = MatteBlack
            )
        ) {
            Text("Logout", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun ProfileCard(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightBlue
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.Blue,
                modifier = Modifier
                    .size(28.dp)
                    .padding(end = 12.dp)
            )
            Column {
                Text(
                    text = label,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = value.ifEmpty { "Not provided" },
                    color = Color.Black,
                    fontSize = 16.sp,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}
