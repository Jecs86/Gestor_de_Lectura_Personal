package com.appstudio.gestordelecturapersonal.ui.screen.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.appstudio.gestordelecturapersonal.data.repository.UserRepository

class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userRepository = UserRepository()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    // ===============================
    // INPUTS
    // ===============================

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    // ===============================
    // LOGIN EMAIL / PASSWORD
    // ===============================

    fun loginWithEmail() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Email y contraseña son obligatorios"
            )
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true)

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                userRepository.saveUserIfNotExists(
                    proveedor = "email"
                ) { success, error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = success,
                        errorMessage = error
                    )
                }
            }
            .addOnFailureListener {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = it.message
                )
            }
    }

    // ===============================
    // GOOGLE SIGN-IN
    // ===============================

    fun loginWithGoogle(idToken: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                userRepository.saveUserIfNotExists(
                    proveedor = "google"
                ) { success, error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = success,
                        errorMessage = error
                    )
                }
            }
            .addOnFailureListener {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = it.message
                )
            }
    }
}
