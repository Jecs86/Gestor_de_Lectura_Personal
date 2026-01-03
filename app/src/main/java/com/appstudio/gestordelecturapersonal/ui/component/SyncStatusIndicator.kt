package com.appstudio.gestordelecturapersonal.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.appstudio.gestordelecturapersonal.domain.sync.SyncState

@Composable
fun SyncStatusIndicator(
    state: SyncState
) {
    when (state) {
        SyncState.IDLE -> return

        SyncState.SYNCING -> {
            SyncBanner(
                text = "Sincronizando cambios...",
                icon = Icons.Default.CloudUpload,
                background = Color(0xFFE3F2FD)
            )
        }

        SyncState.ERROR -> {
            SyncBanner(
                text = "Error al sincronizar",
                icon = Icons.Default.Error,
                background = Color(0xFFFFCDD2)
            )
        }
    }
}

@Composable
private fun SyncBanner(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    background: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}