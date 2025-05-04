package com.example.coffeerustleafclassifier.navigation

sealed class Screen (val route: String){
    data object Welcome : Screen("welcome")
    data object Login : Screen("login")
    data object Register: Screen("register")
    data object ForgotPassword : Screen("forgot_password")
    data object CameraEntryPoint : Screen("camera")
    data object ImagePreview : Screen("image_preview")
    data object ClassificationResult : Screen("classification_result")
}