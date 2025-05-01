package com.example.coffeerustleafclassifier.viewModel

import android.content.Context
import android.credentials.CredentialManager
import android.credentials.GetCredentialException
import android.credentials.GetCredentialResponse
import android.os.Build
import android.os.OutcomeReceiver
import android.service.credentials.GetCredentialRequest
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeerustleafclassifier.R
import com.example.coffeerustleafclassifier.domain.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val credentialManager: CredentialManager,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var loading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun onLoginClick() {
        viewModelScope.launch {
            loading = true
            error = null
            val result = authRepository.loginWithEmail(email, password)
            loading = false
            result.onFailure {
                error = it.message ?: "Unknown error"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) // API 34+
    suspend fun signInWithGoogle() {
    }


}

