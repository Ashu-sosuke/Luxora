package com.example.e_commerse

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.e_commerse.login.AuthViewModel
import com.example.e_commerse.login.LogInScreen
import com.example.e_commerse.login.SignUpPage
import com.example.e_commerse.login.UserPreferences
import com.example.e_commerse.screens.ProfileScreen
import com.example.e_commerse.sub_selection.FashionScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    val authViewModel = remember { AuthViewModel() }

    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.LoginScreen.route) {
            LogInScreen(navController = navController)
        }
        composable(Screen.SignUpScreen.route) {
            SignUpPage(navController = navController)
        }
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.ExploreScreen.route) {
            ExploreScreen()
        }
        composable(Screen.OrderScreen.route) {
            OrdersScreen()
        }
        composable(Screen.WishlistScreen.route) {
            WishlistScreen()
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(
            route = Screen.CategoryScreen.route + "/{categoryName}",
            arguments = listOf(navArgument("categoryName") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryScreen(categoryName)
        }

        composable(Screen.FashionScreen.route){
            FashionScreen()
        }
    }
}



