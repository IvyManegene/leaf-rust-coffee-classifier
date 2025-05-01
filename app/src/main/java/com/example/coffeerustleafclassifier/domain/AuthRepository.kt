package com.example.coffeerustleafclassifier.domain

interface AuthRepository {
    suspend fun loginWithEmail(email: String, password: String): Result<Unit>
    suspend fun loginWithGoogle(idToken: String): Result<Unit>
    fun isUserLoggedIn(): Boolean
}
