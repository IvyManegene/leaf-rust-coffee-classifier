package com.example.coffeerustleafclassifier.auth

import android.content.Context
import androidx.credentials.*
import com.example.coffeerustleafclassifier.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import java.util.UUID

class AuthenticationManager(private val context: Context) {

    private val auth = com.google.firebase.Firebase.auth

    fun createAccountWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthResponse.Success)
                } else {
                    trySend(AuthResponse.Error(message = task.exception?.message ?: ""))
                }
                close()
            }
        awaitClose()
    }

    fun loginInWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthResponse.Success)
                } else {
                    trySend(AuthResponse.Error(message = task.exception?.message ?: ""))
                }
                close()
            }

        awaitClose()
    }

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it ->
            str + "%02x".format(it)
        }
    }

    fun signInWithGoogle(): Flow<AuthResponse> = callbackFlow {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setAutoSelectEnabled(false)
            .setNonce(createNonce())
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try{
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential
            if(credential is CustomCredential){
                if(credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)
                    try{
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)

                        val firebaseCredential = GoogleAuthProvider
                            .getCredential(
                                googleIdTokenCredential.idToken,
                                null
                            )

                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener{
                                if(it.isSuccessful){
                                    trySend(AuthResponse.Success)
                                }else{
                                    trySend(
                                        AuthResponse.Error(
                                            message = it.exception?.message ?: ""
                                        )
                                    )
                                }
                                close()
                            }
                    }catch(e: GoogleIdTokenParsingException){
                        trySend(AuthResponse.Error(message = e.message ?: ""))
                        close()
                    }
            }
        }catch(e: Exception) {
            trySend(AuthResponse.Error(message = e.message ?: ""))

        }
        awaitClose()
    }

    fun sendPasswordResetEmail(email: String): Flow<AuthResponse> = callbackFlow{
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthResponse.Success)
                } else {
                    trySend(
                        AuthResponse.Error(
                            message = task.exception?.message
                                ?: "Failed to send reset email.\nTry again later"
                        )
                    )
                }
                close()
            }
        awaitClose()
    }
}
interface AuthResponse{
    data object Success: AuthResponse
    data class Error(val message: String): AuthResponse
}