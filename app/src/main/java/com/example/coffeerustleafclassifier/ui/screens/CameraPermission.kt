package com.example.coffeerustleafclassifier.ui.screens

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionScreen(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    when {
        cameraPermissionState.status.isGranted -> {
            onPermissionGranted()
        }
        cameraPermissionState.status.shouldShowRationale -> {
            Column {
                Text("Camera permission is needed to use this feature.")
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Grant Permission")
                }
            }
        }
        else -> {
            Text("Permission denied. Please enable it in settings.")
            LaunchedEffect(Unit) {
                onPermissionDenied()
            }
        }
    }
}
