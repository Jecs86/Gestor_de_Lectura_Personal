package com.appstudio.gestordelecturapersonal.ui.screen.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun LoginHeader() {

    Spacer(modifier = androidx.compose.ui.Modifier.height(48.dp))

    Icon(
        imageVector = Icons.Default.MenuBook,
        contentDescription = "App Icon",
        tint = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

    Text(
        text = "Mis Lecturas",
        style = MaterialTheme.typography.headlineMedium
    )

    Text(
        text = "Gestiona tu biblioteca personal",
        style = MaterialTheme.typography.bodyMedium
    )

    Spacer(modifier = androidx.compose.ui.Modifier.height(32.dp))
}
