package com.example.e_commerse

sealed class Screen(val route: String) {
    // Auth & Home
    object SplashScreen : Screen("splash")
    object SignUpScreen : Screen("signup")
    object LoginScreen : Screen("login")
    object HomeScreen : Screen("home")

    // Bottom Navigation Screens
    object ExploreScreen : Screen("explore")
    object OrderScreen : Screen("orders")
    object WishlistScreen : Screen("wishlist")
    object ProfileScreen : Screen("profile")

    // Category Entry
    object CategoryScreen : Screen("category")

    // Subcategories
    object MenFashionScreen : Screen("fashion")
    object WomenFashionScreen: Screen("womenfasion")
    object ElectronicScreen : Screen("electronics")
    object SportScreen : Screen("sport")
    object FurnitureScreen : Screen("furniture")
    object FoodScreen : Screen("food")
    object SmartDevice : Screen("smartdevice")
    object KitchenScreen : Screen("kitchen")

    // Product Navigation
    object ProductListScreen : Screen("product_list/{mainCategory}/{subCategory}")
    object ProductDetailScreen : Screen("product_detail/{productId}")

    // Admin
    object AdminScreen : Screen("admin_screen")
    object AdminLoginScreen : Screen("admin_login")


}
