package com.example.e_commerse.login


import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun signup(
        context: Context,
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        address: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            onError("Email and password must not be empty")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    val user = hashMapOf(
                        "username" to username,
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "email" to email,
                        "address" to address
                    )
                    db.collection("users").document(userId)
                        .set(user)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { onError("Failed to save user data") }
                } else {
                    onError(task.exception?.message ?: "Registration failed")
                }
            }
    }

    fun login(
        context: Context,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Log.d("AuthViewModel", "Login success for $email")
                    onSuccess()
                }
                .addOnFailureListener {
                    Log.e("AuthViewModel", "Login failed", it)
                    onError(it.localizedMessage ?: "Login Failed")
                }
        }
    }

    fun logout(context: Context, onSuccess: () -> Unit) {
        FirebaseAuth.getInstance().signOut()
        UserPreferences(context).clearUser()
        onSuccess()
    }

    private val _userFirstName = mutableStateOf("")
    val userFirstName: State<String> = _userFirstName

    private val _userEmail = mutableStateOf("")
    val userEmail: State<String> = _userEmail

    private val _userLastName = mutableStateOf("")
    val userLastName: State<String> = _userLastName

    private val _userAddress = mutableStateOf("")
    val userAddress: State<String> = _userAddress


    fun fetchUserData(){
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document->
                if (document != null){
                    _userFirstName.value = document.getString("firstName") ?: ""
                    _userLastName.value = document.getString("lastName") ?: ""
                    _userEmail.value = document.getString("email") ?: ""
                    _userAddress.value = document.getString("address") ?: ""


                }
            }
    }
}
