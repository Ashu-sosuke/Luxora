package com.example.e_commerse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.e_commerse.login.SignUpPage
import com.example.e_commerse.ui.theme.ECommerseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ECommerseTheme {
                Navigation()
            }
        }
    }
}
