package com.appstudio.gestordelecturapersonal.ui.screen.login

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Scaffold { paddingValues ->
        LoginContent(
            paddingValues = paddingValues,
            onLoginClick = onLoginClick,
            onRegisterClick = onRegisterClick,
            onForgotPasswordClick = onForgotPasswordClick
        )
    }
}
