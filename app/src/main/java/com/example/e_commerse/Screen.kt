package com.example.e_commerse

sealed class Screen(val route: String) {
    object SplashScreen: Screen("splash")
    object SignUpScreen: Screen("signup")
    object LoginScreen: Screen("login")
    object HomeScreen : Screen("home")
    object ExploreScreen : Screen("explore")
    object OrderScreen : Screen("orders")
    object WishlistScreen : Screen("wishlist")
    object ProfileScreen : Screen("profile")
    object CategoryScreen : Screen("category")

    object FashionScreen: Screen("fashion")

}
