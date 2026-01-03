package com.appstudio.gestordelecturapersonal.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appstudio.gestordelecturapersonal.domain.network.ConnectivityState
import com.appstudio.gestordelecturapersonal.ui.network.ConnectivityViewModel

@Composable
fun ConnectivitySettingsSection() {

    val connectivityViewModel: ConnectivityViewModel = viewModel()
    val state by connectivityViewModel.connectivityState.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        SettingsItem(
            title = "Modo offline",
            subtitle = when (state) {
                ConnectivityState.OFFLINE_FORCED ->
                    "Activado manualmente"
                ConnectivityState.OFFLINE_NO_NETWORK ->
                    "Sin conexión a internet"
                ConnectivityState.ONLINE ->
                    "Conectado"
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = state == ConnectivityState.OFFLINE_FORCED,
                onCheckedChange = { checked ->
                    if (checked) {
                        connectivityViewModel.forceOffline()
                    } else {
                        connectivityViewModel.goOnline()
                    }
                }
            )
        }
    }
}