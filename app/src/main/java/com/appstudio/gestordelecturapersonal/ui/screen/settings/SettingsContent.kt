package com.appstudio.gestordelecturapersonal.ui.screen.settings

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appstudio.gestordelecturapersonal.domain.network.ConnectivityState
import com.appstudio.gestordelecturapersonal.ui.network.ConnectivityViewModel


@Composable
fun SettingsContent(
    paddingValues: PaddingValues,
    email: String?,
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
                subtitle = "Claro / Oscuro"
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
