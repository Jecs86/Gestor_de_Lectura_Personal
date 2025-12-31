package com.appstudio.gestordelecturapersonal.ui.screen.settings

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.appstudio.gestordelecturapersonal.ui.component.AppBottomBar
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appstudio.gestordelecturapersonal.ui.component.AppTopBar

@Composable
fun SettingsScreen(
    navController: NavController,
    onLogoutSuccess: () -> Unit
) {
    val viewModel: SettingsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onLogoutSuccess()
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Configuración"
            )
        },
        bottomBar = {
            AppBottomBar(navController)
        }
    ) { paddingValues ->
        SettingsContent(
            paddingValues = paddingValues,
            email = uiState.email,
            onLogoutClick = viewModel::logout
        )
    }
}
