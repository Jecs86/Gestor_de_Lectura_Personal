package com.appstudio.gestordelecturapersonal.ui.screen.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
