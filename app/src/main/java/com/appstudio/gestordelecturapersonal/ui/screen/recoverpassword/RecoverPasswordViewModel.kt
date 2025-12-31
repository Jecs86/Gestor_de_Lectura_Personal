package com.appstudio.gestordelecturapersonal.ui.screen.recoverpassword

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RecoverPasswordViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(RecoverPasswordUiState())
    val uiState: StateFlow<RecoverPasswordUiState> = _uiState

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun sendPasswordResetEmail() {
        val email = _uiState.value.email.trim()

        if (email.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Ingresa tu correo"
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null,
            successMessage = null
        )

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Te enviamos un correo para restablecer tu contraseña"
                )
            }
            .addOnFailureListener {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = it.message
                )
            }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}
