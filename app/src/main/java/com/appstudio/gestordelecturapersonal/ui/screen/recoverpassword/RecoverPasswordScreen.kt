package com.appstudio.gestordelecturapersonal.ui.screen.recoverpassword

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appstudio.gestordelecturapersonal.ui.component.AppTopBar
import com.appstudio.gestordelecturapersonal.ui.component.CustomLoadingOverlay
import com.appstudio.gestordelecturapersonal.ui.component.NotificationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoverPasswordScreen(
    onBackToLogin: () -> Unit,
    onShowSnackbar: (String, NotificationType) -> Unit,
    viewModel: RecoverPasswordViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        uiState.errorMessage?.let {
            onShowSnackbar(it, NotificationType.Error)
            viewModel.clearMessages()
        }
        uiState.successMessage?.let {
            onShowSnackbar(it, NotificationType.Saved)
            viewModel.clearMessages()
            onBackToLogin()
        }
    }

    Box(modifier = Modifier.fillMaxSize()){

        Scaffold(
            topBar = {
                AppTopBar(
                    title = "Recuperar contraseña",
                    needBackPage = true,
                    onBackPage = onBackToLogin
                )
            }
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    label = { Text("Correo electrónico") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = viewModel::sendPasswordResetEmail,
                    enabled = !uiState.isLoading,
                ) {
                    Text(text = "Enviar correo")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onBackToLogin,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                ) {
                    Text("Volver al login")
                }
            }
        }

        CustomLoadingOverlay(
            isLoading = uiState.isLoading,
            message = "Enviando Solicitud..."
        )

    }
}
