package com.appstudio.gestordelecturapersonal.ui.screen.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false,
    val showResendVerification: Boolean = false
)
