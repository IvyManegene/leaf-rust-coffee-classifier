package com.example.coffeerustleafclassifier.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.coffeerustleafclassifier.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePreviewScreen(
    uri: Uri,
    onRetake: () -> Unit,
    navController: NavController
) {

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Image Preview") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = uri)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = "Captured Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Fit
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = onRetake) {
                    Icon(Icons.Default.Close, contentDescription = "Retake")
                }
                IconButton(onClick = {
                    navController.navigate("${Screen.ClassificationResult.route}?uri=${Uri.encode(uri.toString())}")
                }) {
                    Icon(Icons.Default.Check, contentDescription = "Confirm")
                }
            }
        }
    }
}
