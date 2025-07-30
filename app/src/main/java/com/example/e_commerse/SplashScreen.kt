package com.example.e_commerse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.e_commerse.login.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    val userPrefs = remember { UserPreferences(context) }

    val db = FirebaseFirestore.getInstance()
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1500)
        val rememberMe = userPrefs.getRememberMe()

        if (rememberMe && user != null) {
            val uid = user.uid
            db.collection("admins").document(uid).get()
                .addOnSuccessListener { document ->
                    isLoading = false
                    if (document.exists() && document.getString("role") == "admin") {
                        navController.navigate("admin_screen") {
                            popUpTo(Screen.SplashScreen.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.HomeScreen.route) {
                            popUpTo(Screen.SplashScreen.route) { inclusive = true }
                        }
                    }
                }
                .addOnFailureListener {
                    isLoading = false
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                }
        } else {
            isLoading = false
            navController.navigate(Screen.LoginScreen.route) {
                popUpTo(Screen.SplashScreen.route) { inclusive = true }
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
