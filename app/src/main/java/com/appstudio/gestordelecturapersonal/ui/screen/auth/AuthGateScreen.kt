package com.appstudio.gestordelecturapersonal.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AuthGateScreen(
    onAuthenticated: () -> Unit,
    onUnauthenticated: () -> Unit,
    viewModel: AuthGateViewModel
) {
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    when (isAuthenticated) {
        true -> onAuthenticated()
        false -> onUnauthenticated()
        null -> {}
    }

}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary), // O el color de tu marca
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Mis Lecturas",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}