package com.example.coffeerustleafclassifier.ui.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.coffeerustleafclassifier.R
import coil.compose.rememberAsyncImagePainter
import com.example.coffeerustleafclassifier.viewModel.CameraViewModel
import java.io.File

@Composable
fun CameraEntryPointScreen(
    navController: NavController,
    cameraViewModel: CameraViewModel = viewModel(),
    onImageCaptured : (Uri) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var permissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val imageUri by cameraViewModel.capturedImageUri.collectAsState()

    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imageUri?.let { uri ->
                cameraViewModel.setCapturedImage(uri)
                onImageCaptured(uri)
            }
        }
    }


    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) {permissions->
        val readGranted = permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: false
        val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false

        if (readGranted && cameraGranted){
            val uri = createImageUri(context)
            cameraViewModel.setCapturedImage(uri)
            takePictureLauncher.launch(uri)
            permissionGranted = true
        }else{
            Toast.makeText(context,"Camera Permission denied",Toast.LENGTH_SHORT).show()
        }
    }
    if (permissionGranted) {
        CameraScreen(
            lifecycleOwner = lifecycleOwner,
            viewModel = cameraViewModel,
            navController = navController
        )
    } else {
        CameraPermissionScreen(
            onPermissionGranted = {
                permissionGranted = true
            },
            onPermissionDenied = {
                // Pop back if permission denied
                navController.popBackStack()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Captured Image",
                modifier = Modifier
                    .size(250.dp)
                    .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.image_placeholder),
                contentDescription = "Placeholder Image",
                modifier = Modifier
                    .size(250.dp)
                    .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
        }) {
            Text("Capture Image")
        }
    }
}


private fun createImageUri(context: Context): Uri {
    val imageFile = File(context.cacheDir, "captured_image.jpg")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )
}