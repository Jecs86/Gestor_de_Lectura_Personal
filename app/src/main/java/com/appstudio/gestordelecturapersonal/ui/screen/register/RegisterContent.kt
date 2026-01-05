package com.appstudio.gestordelecturapersonal.ui.screen.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegisterContent(
    paddingValues: PaddingValues,
    uiState: RegisterUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center
    ) {

        RegisterForm(
            email = uiState.email,
            password = uiState.password,
            confirmPassword = uiState.confirmPassword,
            isLoading = uiState.isLoading,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onConfirmPasswordChange = onConfirmPasswordChange,
            onRegisterClick = onRegisterClick
        )
    }
}