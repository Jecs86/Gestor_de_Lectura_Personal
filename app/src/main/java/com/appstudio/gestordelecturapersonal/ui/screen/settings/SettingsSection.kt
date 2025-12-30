package com.appstudio.gestordelecturapersonal.ui.screen.settings


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Spacer(modifier = androidx.compose.ui.Modifier.height(24.dp))

    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))

    Column {
        content()
    }
}
