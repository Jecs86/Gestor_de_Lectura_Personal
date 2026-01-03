package com.appstudio.gestordelecturapersonal.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.appstudio.gestordelecturapersonal.domain.network.ConnectivityState

@Composable
fun OfflineBanner(
    state: ConnectivityState
) {
    if (state == ConnectivityState.ONLINE) return

    val text = when (state) {
        ConnectivityState.OFFLINE_FORCED ->
            "Modo offline activado"

        ConnectivityState.OFFLINE_NO_NETWORK ->
            "Sin conexión a internet"

        else -> ""
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF3E0))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.CloudOff, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}