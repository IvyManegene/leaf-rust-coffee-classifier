package com.example.coffeerustleafclassifier.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coffeerustleafclassifier.ui.screens.LoginScreen
import com.example.coffeerustleafclassifier.ui.screens.RegisterScreen
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
            RegisterScreen()
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClicked = { /* Handle login button click */ },
                onGoogleLoginClicked = { /* Handle Google login button click */ },
                onSignUpClicked = { navController.navigate(Screen.Register.route) },
                onForgotPasswordClicked = { /* Handle forgot password button click */ }
            )
        }
    }
}