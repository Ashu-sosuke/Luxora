package com.example.e_commerse

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore


fun payUsingUPI(
    activity: Activity,
    upiId: String,
    name: String,
    note: String,
    amount: String,
    requestCode: Int = 123
) {
    val uri = Uri.parse("upi://pay").buildUpon()
        .appendQueryParameter("pa", upiId) // UPI ID
        .appendQueryParameter("pn", name) // Payee Name
        .appendQueryParameter("tn", note) // Transaction Note
        .appendQueryParameter("am", amount) // Amount
        .appendQueryParameter("cu", "INR") // Currency
        .build()

    val upiPayIntent = Intent(Intent.ACTION_VIEW).apply {
        data = uri
    }

    val chooser = Intent.createChooser(upiPayIntent, "Pay with UPI")
    if (chooser.resolveActivity(activity.packageManager) != null) {
        activity.startActivityForResult(chooser, requestCode)
    } else {
        Toast.makeText(activity, "No UPI app found", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun getActivity(): Activity {
    val context = LocalContext.current
    return remember(context) {
        generateSequence(context) {
            if (it is android.content.ContextWrapper) it.baseContext else null
        }.filterIsInstance<Activity>().first()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(productId: String, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var product by remember { mutableStateOf<Product?>(null) }
    val activity = getActivity()


    LaunchedEffect(productId) {
        db.collection("products").document(productId)
            .get()
            .addOnSuccessListener { doc ->
                product = doc.toObject(Product::class.java)
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment", color = NeonGreen) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.DarkGray)
            )
        },
        containerColor = MatteBlack
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            product?.let {
                Column {
                    Text("You are about to buy:", color = Color.White, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it.name, color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Price: ₹${it.price}", color = Color.White, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            Button(
                onClick = {
                    payUsingUPI(
                        activity = activity,
                        upiId = "15bhartiashutosh@okicici",
                        name = "Luxora",
                        note = "Payment for ${product?.name}",
                        amount = product?.price.toString()
                    )
                    navController.navigate("order_success")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
            ) {
                Text("Pay Now", color = Color.Black)
            }
        }
    }
}
