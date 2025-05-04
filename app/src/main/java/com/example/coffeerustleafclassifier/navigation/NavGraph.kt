package com.example.coffeerustleafclassifier.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.coffeerustleafclassifier.ui.screens.CameraEntryPointScreen
import com.example.coffeerustleafclassifier.ui.screens.ClassificationResultScreen
import com.example.coffeerustleafclassifier.ui.screens.ForgotPasswordScreen
import com.example.coffeerustleafclassifier.ui.screens.ImagePreviewScreen
import com.example.coffeerustleafclassifier.ui.screens.LoginScreen
import com.example.coffeerustleafclassifier.ui.screens.RegistrationScreen
import com.example.coffeerustleafclassifier.ui.screens.WelcomeScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Welcome.route
    )
    {
        composable(Screen.Welcome.route) {
            WelcomeScreen(onStartClicked = { navController.navigate(Screen.Login.route) })
        }

        composable(Screen.Register.route) {
            RegistrationScreen(
                onLoggedIn = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoggedIn = {
                    navController.navigate(Screen.CameraEntryPoint.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onSignUpClicked = { navController.navigate(Screen.Register.route) },
                onForgotPasswordClicked = { /* Handle forgot password button click */ }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackToLoginClicked = { navController.navigate(Screen.Login.route) }
            )
        }

        composable(Screen.CameraEntryPoint.route) {
            CameraEntryPointScreen(
                navController = navController,
                onImageCaptured = {
                    navController.navigate(Screen.ClassificationResult.route)
                }
            )
        }

        composable(Screen.ImagePreview.route + "/{imageUri}") { backStackEntry ->
            val uri = Uri.parse(backStackEntry.arguments?.getString("imageUri"))
            ImagePreviewScreen(
                uri = uri,
                onRetake = {
                    navController.popBackStack()
                },
                navController = navController
            )
        }

        composable(
            route = "${Screen.ClassificationResult.route}?uri={uri}",
            arguments = listOf(navArgument("uri") { type = NavType.StringType })
        ) { backStackEntry ->
            val uriString = backStackEntry.arguments?.getString("uri")
            val uri = uriString?.let { Uri.parse(it) }

            if (uri != null) {
                ClassificationResultScreen(uri = uri)
            }
        }


    }
}