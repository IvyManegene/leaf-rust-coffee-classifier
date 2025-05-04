package com.example.coffeerustleafclassifier.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.coffeerustleafclassifier.model.ModelClassifier
import com.example.coffeerustleafclassifier.utils.uriToBitmap

@Composable
fun ClassificationResultScreen(uri: Uri) {
    val context = LocalContext.current
    var result by remember { mutableStateOf("Classifying...") }

    Column {
        if (result == "Classifying...") {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterHorizontally),
                color = Color.White
            )
        }
    }
    LaunchedEffect(uri) {
        val bitmap = uriToBitmap(context, uri)
        val classifier = ModelClassifier(context)
        result = classifier.classify(bitmap)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = rememberAsyncImagePainter(uri),
            contentDescription = "Captured Image",
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xAA000000))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val (icon, color) = when (result) {
                    "Healthy" -> Icons.Default.CheckCircle to Color(0xFF4CAF50) // Green
                    "Rust-Affected" -> Icons.Default.Warning to Color(0xFFFFC107) // Yellow
                    else -> Icons.Default.Warning to Color.Red
                }

                Icon(
                    imageVector = icon,
                    contentDescription = "Status Icon",
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Prediction: $result",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
