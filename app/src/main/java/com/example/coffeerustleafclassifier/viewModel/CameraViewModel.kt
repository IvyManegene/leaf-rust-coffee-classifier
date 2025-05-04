package com.example.coffeerustleafclassifier.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CameraViewModel : ViewModel() {
    private val _capturedImageUri = MutableStateFlow<Uri?>(null)
    val capturedImageUri: StateFlow<Uri?> = _capturedImageUri

    fun setCapturedImage(uri: Uri) {
        _capturedImageUri.value = uri
    }

    fun clearCapturedImage(){
        _capturedImageUri.value = null
    }
}
