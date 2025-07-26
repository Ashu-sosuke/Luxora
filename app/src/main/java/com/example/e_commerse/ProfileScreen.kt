package com.example.e_commerse.screens

import androidx.compose.foundation.background
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
import androidx.navigation.NavController
import com.example.e_commerse.login.AuthViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current

    // Fetch user data once when screen is composed
    LaunchedEffect(Unit) {
        authViewModel.fetchUserData()
    }

    val firstName by authViewModel.userFirstName
    val lastName by authViewModel.userLastName
    val email by authViewModel.userEmail
    val address by authViewModel.userAddress

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.Black // matte black background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "User Profile",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 32.dp, top = 16.dp)
            )

            ProfileItem(label = "First Name:", value = firstName)
            ProfileItem(label = "Last Name:", value = lastName)
            ProfileItem(label = "Email:", value = email)
            ProfileItem(label = "Address:", value = address)

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    authViewModel.logout(context) {
                        navController.navigate("LoginScreen") {
                            popUpTo("ProfileScreen") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White
                )
            ) {
                Text("Logout")
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Text(
        text = "$label $value",
        color = Color.White,
        fontSize = 16.sp,
        modifier = Modifier.padding(vertical = 6.dp)
    )
}
