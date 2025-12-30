package com.appstudio.gestordelecturapersonal.ui.screen.register

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit
) {
    val viewModel: RegisterViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isRegistered) {
        onRegisterSuccess()
    }

    Scaffold { paddingValues ->
        RegisterContent(
            paddingValues = paddingValues,
            uiState = uiState,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
            onRegisterClick = viewModel::registerWithEmail
        )
    }
}