package com.example.e_commerse.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.e_commerse.NeonGreen
import com.example.e_commerse.Screen
import com.example.e_commerse.screens.MatteBlack
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdminLoginScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var rememberMe by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val userPrefs = remember { UserPreferences(context) }
    val viewModel: AuthViewModel = viewModel()

    LaunchedEffect(Unit) {
        rememberMe = userPrefs.getRememberMe()
        if (rememberMe) {
            email = userPrefs.getEmail()
            password = userPrefs.getPassword()

            if (email.isNotBlank() && password.isNotBlank()) {
                loading = true
                auth.signInWithEmailAndPassword(email.trim(), password.trim())
                    .addOnSuccessListener { result ->
                        val uid = result.user?.uid
                        checkAdminAndNavigate(uid, db, navController, context, rememberMe, email, password, userPrefs)
                        loading = false
                    }
                    .addOnFailureListener {
                        loading = false
                        Toast.makeText(context, "Login Failed: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Admin Login", style = MaterialTheme.typography.headlineSmall, color = NeonGreen)

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp)
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(checkedColor = NeonGreen)
                )
                Text("Remember Me", color = Color.Black)
            }

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    loading = true
                    auth.signInWithEmailAndPassword(email.trim(), password.trim())
                        .addOnSuccessListener { result ->
                            val uid = result.user?.uid
                            checkAdminAndNavigate(uid, db, navController, context, rememberMe, email, password, userPrefs)
                            loading = false
                        }
                        .addOnFailureListener {
                            loading = false
                            Toast.makeText(context, "Login Failed: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }


            if (loading) {
                CircularProgressIndicator()
            }
        }
    }
}

fun checkAdminAndNavigate(
    uid: String?,
    db: FirebaseFirestore,
    navController: NavController,
    context: android.content.Context,
    rememberMe: Boolean,
    email: String,
    password: String,
    userPrefs: UserPreferences
) {
    if (uid == null) {
        Toast.makeText(context, "Invalid User", Toast.LENGTH_SHORT).show()
        return
    }

    db.collection("admins").document(uid).get()
        .addOnSuccessListener { doc ->
            val role = doc.getString("role")
            if (role == "admin") {
                if (rememberMe) {
                    userPrefs.saveUser(email, password, rememberMe)
                } else {
                    userPrefs.clearUser()
                }
                navController.navigate("admin_screen") {
                    popUpTo(Screen.LoginScreen.route) { inclusive = true }
                }
            } else {
                Toast.makeText(context, "Access Denied: Not an Admin", Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance().signOut()
            }

        }
        .addOnFailureListener {
            Toast.makeText(context, "Error verifying admin: ${it.message}", Toast.LENGTH_LONG).show()
        }
}
