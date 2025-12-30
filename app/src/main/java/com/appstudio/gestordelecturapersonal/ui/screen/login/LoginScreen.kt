package com.appstudio.gestordelecturapersonal.ui.screen.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.appstudio.gestordelecturapersonal.ui.navigation.AppRoutes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.appstudio.gestordelecturapersonal.ui.screen.login.auth.GoogleAuthClient


@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: () -> Unit,
) {
    val context = LocalContext.current
    val viewModel: LoginViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    val googleAuthClient = remember { GoogleAuthClient(context) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        if (task.isSuccessful) {
            val account = task.result
            viewModel.loginWithGoogle(account.idToken!!)
        }
    }

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onLoginSuccess()
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LoginContent(
                paddingValues = paddingValues,
                email = uiState.email,
                password = uiState.password,
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onLoginClick = viewModel::loginWithEmail,
                onRegisterClick = {
                    navController.navigate(AppRoutes.Register.route)
                },
                onForgotPasswordClick = {/* TODO */},
                onGoogleLoginClick = {
                    launcher.launch(
                        googleAuthClient.googleSignInClient.signInIntent
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            GoogleSignInButton(
                enabled = !uiState.isLoading,
                onClick = {
                    launcher.launch(
                        googleAuthClient.googleSignInClient.signInIntent
                    )
                }
            )
        }
    }
}

