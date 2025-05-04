package com.example.coffeerustleafclassifier.ui.screens

import androidx.compose.runtime.*
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.coffeerustleafclassifier.ui.view.LiveCameraView
import com.example.coffeerustleafclassifier.viewModel.CameraViewModel

@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavController
) {
    val imageUri by viewModel.capturedImageUri.collectAsState()

    if (imageUri != null) {
        ImagePreviewScreen(
            uri = imageUri!!,
            onRetake = { viewModel.clearCapturedImage() },
            navController = navController
        )
    } else {
        LiveCameraView(
            lifecycleOwner = lifecycleOwner,
            onImageCaptured = { uri -> viewModel.setCapturedImage(uri) },
            onPickImageFromGallery = { uri -> viewModel.setCapturedImage(uri) }
        )
    }
}

