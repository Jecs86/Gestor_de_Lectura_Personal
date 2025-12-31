package com.appstudio.gestordelecturapersonal.ui.screen.recoverpassword

data class RecoverPasswordUiState (
    val email: String = "",
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)