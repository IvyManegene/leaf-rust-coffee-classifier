package com.example.coffeerustleafclassifier
import android.app.Application
import com.google.firebase.FirebaseApp
class CoffeeRustClassifierApp : Application() {
     override fun onCreate() {
            super.onCreate()
            FirebaseApp.initializeApp(this)
        }
 }