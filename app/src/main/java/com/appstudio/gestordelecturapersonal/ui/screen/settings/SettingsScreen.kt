package com.appstudio.gestordelecturapersonal.ui.screen.settings

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.appstudio.gestordelecturapersonal.ui.component.AppBottomBar
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appstudio.gestordelecturapersonal.data.local.db.DatabaseProvider
import com.appstudio.gestordelecturapersonal.data.repository.SyncRepository
import com.appstudio.gestordelecturapersonal.ui.component.AppTopBar
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SettingsScreen(
    navController: NavController,
    onLogoutSuccess: () -> Unit
) {

    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)

    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(
            database = database,
            syncRepository = SyncRepository(database, FirebaseFirestore.getInstance())
        )
    )
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
