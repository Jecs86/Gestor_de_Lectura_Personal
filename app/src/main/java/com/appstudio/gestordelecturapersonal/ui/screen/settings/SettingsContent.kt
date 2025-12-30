package com.appstudio.gestordelecturapersonal.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SettingsContent(
    paddingValues: PaddingValues,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {

        SettingsSection(title = "Cuenta") {
            SettingsItem(
                title = "Editar perfil",
                subtitle = "Nombre, foto, correo",
                onClick = { /* navegar a perfil */ }
            )

            SettingsItem(
                title = "Cerrar sesión",
                subtitle = "Salir de la aplicación",
                onClick = onLogoutClick,
                isDestructive = true
            )
        }

        SettingsSection(title = "Preferencias") {
            SettingsItem(
                title = "Tema",
                subtitle = "Claro / Oscuro"
            )
            SettingsItem(
                title = "Idioma",
                subtitle = "Español"
            )
        }

        SettingsSection(title = "Información") {
            SettingsItem(
                title = "Acerca de",
                subtitle = "Versión de la app"
            )
        }
    }
}
