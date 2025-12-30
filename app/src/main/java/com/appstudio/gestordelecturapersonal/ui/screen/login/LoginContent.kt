package com.appstudio.gestordelecturapersonal.ui.screen.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoginContent(
    paddingValues: PaddingValues,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginHeader()
        LoginForm(
            onLoginClick = onLoginClick,
            onForgotPasswordClick = onForgotPasswordClick
        )
        LoginFooter(
            onRegisterClick = onRegisterClick
        )
    }
}
