package com.example.e_commerse

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.e_commerse.admin.AdminUploadScreen
import com.example.e_commerse.login.*
import com.example.e_commerse.sub_selection.*

import java.net.URLDecoder

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val authViewModel = remember { AuthViewModel() }

    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {

        // ----------------------------
        // 🔹 AUTH SCREENS
        // ----------------------------
        composable(Screen.SplashScreen.route) { SplashScreen(navController) }
        composable(Screen.LoginScreen.route) { LogInScreen(navController) }
        composable(Screen.SignUpScreen.route) { SignUpPage(navController) }

        // ----------------------------
        // 🔹 HOME & MAIN SCREENS
        // ----------------------------
        composable(Screen.HomeScreen.route) { HomeScreen(navController) }
        composable(Screen.ExploreScreen.route) { ExploreScreen(navController) }
        composable(Screen.OrderScreen.route) { OrdersScreen(navController) }
        composable(Screen.WishlistScreen.route) { WishlistScreen(navController) }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(navController = navController, authViewModel = authViewModel)
        }

        // ----------------------------
        // 🔹 CATEGORY SCREENS
        // ----------------------------
        composable(Screen.FashionScreen.route) { FashionScreen(navController) }
        composable(Screen.ElectronicScreen.route) { ElectronicScreen(navController) }
        composable(Screen.SportScreen.route) { SportScreen(navController) }
        composable(Screen.FurnitureScreen.route) { FurnitureScreen(navController) }
        composable(Screen.FoodScreen.route) { FoodScreen(navController) }
        composable(Screen.SmartDevice.route) { SmartDevice(navController) }
        composable(Screen.KitchenScreen.route) { KitchenScreen(navController) }

        // ----------------------------
        // 🔹 PRODUCT LIST SCREEN (Dynamic from Firestore)
        // ----------------------------
        composable(
            route = "product_list/{mainCategory}/{subCategory}",
            arguments = listOf(
                navArgument("mainCategory") { type = NavType.StringType },
                navArgument("subCategory") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mainCategory = URLDecoder.decode(
                backStackEntry.arguments?.getString("mainCategory") ?: "",
                "UTF-8"
            )
            val subCategory = URLDecoder.decode(
                backStackEntry.arguments?.getString("subCategory") ?: "",
                "UTF-8"
            )

            ProductListScreen(
                mainCategory = mainCategory,
                subCategory = subCategory,
                navController = navController
            )
        }

        // ----------------------------
        // 🔹 PRODUCT DETAIL SCREEN (Dynamic Firestore)
        // ----------------------------
        composable(
            route = "product_detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedId = backStackEntry.arguments?.getString("productId") ?: ""
            val decodedId = URLDecoder.decode(encodedId, "UTF-8")

            ProductDetailScreen(
                productId = decodedId,
                navController = navController
            )
        }

        // ----------------------------
        // 🔹 PAYMENT SCREEN (Optional Future Extension)
        // ----------------------------
        composable(
            route = "payment/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            PaymentScreen(productId = productId, navController = navController)
        }

        // ----------------------------
        // 🔹 ADMIN SCREENS
        // ----------------------------
        composable("admin_login") { AdminLoginScreen(navController) }
        composable("admin_screen") { AdminUploadScreen(navController) }
    }
}
