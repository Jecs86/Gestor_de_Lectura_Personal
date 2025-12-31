package com.appstudio.gestordelecturapersonal.ui.screen.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AuthGateScreen(
    onAuthenticated: () -> Unit,
    onUnauthenticated: () -> Unit
) {
    val viewModel: AuthGateViewModel = viewModel()
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    when (isAuthenticated) {
        true -> onAuthenticated()
        false -> onUnauthenticated()
        null -> {
            // Loading simple
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}