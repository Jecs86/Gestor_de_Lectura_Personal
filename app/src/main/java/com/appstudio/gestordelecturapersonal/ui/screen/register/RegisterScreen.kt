package com.appstudio.gestordelecturapersonal.ui.screen.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appstudio.gestordelecturapersonal.ui.component.AppTopBar
import com.appstudio.gestordelecturapersonal.ui.component.CustomLoadingOverlay
import com.appstudio.gestordelecturapersonal.ui.component.NotificationType

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onShowSnackbar: (String, NotificationType) -> Unit
) {
    val viewModel: RegisterViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            onShowSnackbar(it, NotificationType.Error)
            viewModel.clearError()
        }
    }
    LaunchedEffect(uiState.isRegistered) {
        if (uiState.isRegistered) {
            onShowSnackbar("Cuenta creada. Por favor, verifica tu correo.", NotificationType.Success)
            onRegisterSuccess()
        }
    }
    Box(modifier = Modifier.fillMaxSize()){
        Scaffold(
            topBar = {
                AppTopBar(
                    title = "Registro",
                    needBackPage = true,
                    onBackPage = onRegisterSuccess
                )
            }
        ) { paddingValues ->
            RegisterContent(
                paddingValues = paddingValues,
                uiState = uiState,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                onRegisterClick = viewModel::registerWithEmail
            )
        }

        CustomLoadingOverlay(
            isLoading = uiState.isLoading,
            message = "Creando cuenta..."
        )

    }
}