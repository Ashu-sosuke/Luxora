package com.example.e_commerse

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.delay

data class Post(
        val imgUrl: String = "",
        val link: String = ""
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ImageSlider() {
        val context = LocalContext.current
        val db = FirebaseFirestore.getInstance()
        val posts = remember { mutableStateListOf<Post>() }

        LaunchedEffect(Unit) {
                db.collection("posts")
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .get()
                        .addOnSuccessListener { result ->
                                posts.clear()
                                for (document in result) {
                                        val imageUrl = document.getString("imageUrl") ?: ""
                                        val link = document.getString("link") ?: "" // lowercase
                                        posts.add(Post(imageUrl, link))
                                }
                        }
                        .addOnFailureListener {
                                Log.e("Firestore", "Error: ${it.message}")
                        }
        }

        var currentPage by remember { mutableStateOf(0) }

        LaunchedEffect(posts.size) {
                if (posts.isNotEmpty()) {
                        while (true) {
                                delay(3000)
                                currentPage = (currentPage + 1) % posts.size
                        }
                }
        }

        if (posts.isNotEmpty()) {
                Box(
                        modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                ) {
                        AnimatedContent(
                                targetState = currentPage,
                                transitionSpec = {
                                        fadeIn(animationSpec = tween(1000)) togetherWith
                                                fadeOut(animationSpec = tween(1000))
                                },
                                label = "CrossfadeSlider"
                        ) { page ->
                                val post = posts[page]
                                Card(
                                        modifier = Modifier
                                                .fillMaxSize()
                                                .clickable {
                                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.link))
                                                        context.startActivity(intent)
                                                }
                                ) {
                                        AsyncImage(
                                                model = post.imgUrl,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                        )
                                }
                        }

                        Row(
                                modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 10.dp)
                                        .background(Color(0x66000000), shape = CircleShape)
                                        .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                        ) {
                                repeat(posts.size) { index ->
                                        Box(
                                                modifier = Modifier
                                                        .size(if (index == currentPage) 10.dp else 8.dp)
                                                        .clip(CircleShape)
                                                        .background(
                                                                if (index == currentPage) Color.White else Color.LightGray
                                                        )
                                                        .padding(horizontal = 3.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                }
                        }
                }
        }
}
