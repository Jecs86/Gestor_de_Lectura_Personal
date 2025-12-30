package com.appstudio.gestordelecturapersonal.ui.screen.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun LoginFooter(
    onRegisterClick: () -> Unit
) {
    Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

    TextButton(
        onClick = onRegisterClick
    ) {
        Text(
            text = "¿No tienes cuenta? Regístrate",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
