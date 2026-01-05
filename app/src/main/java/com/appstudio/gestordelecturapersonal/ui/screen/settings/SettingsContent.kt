package com.appstudio.gestordelecturapersonal.ui.screen.settings

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.appstudio.gestordelecturapersonal.R


@Composable
fun SettingsContent(
    paddingValues: PaddingValues,
    email: String?,
    onLogoutClick: () -> Unit,
    themeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
    context: Context
) {

    var showThemeDialog by remember { mutableStateOf(false) }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = themeMode,
            onDismiss = { showThemeDialog = false },
            onThemeSelected = {
                onThemeChange(it)
                showThemeDialog = false
            }
        )
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {

        SettingsSection(title = "Cuenta") {
            SettingsItem(
                title = email ?: "Sin sesión activa",
                subtitle = "Cuenta conectada"
            )

            SettingsItem(
                title = "Cerrar sesión",
                subtitle = "Salir de la aplicación",
                onClick = onLogoutClick,
                isDestructive = true
            )
        }

        SettingsSection(title = "Conectividad") {
            ConnectivitySettingsSection()
        }

        SettingsSection(title = "Preferencias") {
            SettingsItem(
                title = "Tema",
                subtitle = when(themeMode) {
                    ThemeMode.SYSTEM -> "Predeterminado del sistema"
                    ThemeMode.LIGHT -> "Claro"
                    ThemeMode.DARK -> "Oscuro"
                },
                onClick = { showThemeDialog = true }
            )
        }

        SettingsSection(title = "Información") {
            SettingsItem(
                title = "Acerca de",
                subtitle = "Versión de la app ${context.getString(R.string.version_app)}"
            )
        }
    }
}
