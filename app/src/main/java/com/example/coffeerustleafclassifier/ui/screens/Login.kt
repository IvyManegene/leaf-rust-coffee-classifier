package com.example.coffeerustleafclassifier.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.coffeerustleafclassifier.R
import com.example.coffeerustleafclassifier.viewModel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onSignUpClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {

        Text(
            text = "LOGIN",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            fontSize = 30.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Text(
                text = "Access Account",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Log in to detect Coffee Rust disease for your coffee crops",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = { Text("Email Address") },
                leadingIcon = {Icon(Icons.Default.Email, contentDescription = "johndoe@gmail.com")},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.password,
                onValueChange = {viewModel.password = it},
                label = { Text("Password") },
                leadingIcon = {Icon(Icons.Default.Lock, contentDescription = null)},
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            TextButton(
                onClick = onForgotPasswordClicked,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Forgot Password?")

            }

            Button(
                onClick = { viewModel.onLoginClick()},
                enabled = !viewModel.loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Log In",
                    color = Color.White
                )
            }
            if(viewModel.error != null)
            {
                Text(
                    text = viewModel.error ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text("Or", color = Color.Gray)
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    scope.launch {
                    val result = viewModel.signInWithGoogle()
                    if (result.isSuccess){
                        navController.navigate("home"){
                            popUpTo("login"){inclusive = true}
                        }
                    }else{
                        val message = result.exceptionOrNull()?.message ?: "Google Sign-In failed"
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                    }
                    }},
                modifier = Modifier
                    .fillMaxWidth(),
                border = BorderStroke(1.dp, Color.Gray),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logo_google_icon),
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))
                Text("Continue with Google")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row{
                Text("Need to create an account?")
                Text(
                    "Sign Up",
                    modifier = Modifier.clickable (onClick = onSignUpClicked )
                        .padding(start = 4.dp),
                    color = Color(0xFF556B2F),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "By signing up you agree to our Terms and Conditions and Privacy Policy",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
            )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onSignUpClicked = {},
        onForgotPasswordClicked = {}
    )
}
