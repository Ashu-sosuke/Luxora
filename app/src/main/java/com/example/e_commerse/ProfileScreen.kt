package com.example.e_commerse.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.e_commerse.Screen
import com.example.e_commerse.login.AuthViewModel

val MatteBlack = Color(0xFF121212)
val NeonGreen = Color(0xFF00FF9C)


@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        authViewModel.fetchUserData()
    }

    val firstName by authViewModel.userFirstName
    val lastName by authViewModel.userLastName
    val email by authViewModel.userEmail
    val address by authViewModel.userAddress

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MatteBlack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Profile",
                color = NeonGreen,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp, top = 12.dp)
            )

            ProfileCard(icon = Icons.Default.Person, label = "First Name", value = firstName)
            ProfileCard(icon = Icons.Default.Person, label = "Last Name", value = lastName)
            ProfileCard(icon = Icons.Default.Email, label = "Email", value = email)
            ProfileCard(icon = Icons.Default.LocationOn, label = "Address", value = address)

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    authViewModel.logout(context) {
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo("ProfileScreen") { inclusive = true }
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
}

@Composable
fun ProfileCard(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F1F1F)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = NeonGreen,
                modifier = Modifier
                    .size(28.dp)
                    .padding(end = 12.dp)
            )
            Column {
                Text(
                    text = label,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
