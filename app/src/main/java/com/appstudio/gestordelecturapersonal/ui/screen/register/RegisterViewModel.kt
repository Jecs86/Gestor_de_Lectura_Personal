package com.appstudio.gestordelecturapersonal.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstudio.gestordelecturapersonal.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.Timestamp

class RegisterViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = value)
    }

    fun registerWithEmail() {
        val state = _uiState.value

        if (state.password != state.confirmPassword) {
            _uiState.value = state.copy(
                errorMessage = "Las contraseñas no coinciden"
            )
            return
        }

        _uiState.value = state.copy(isLoading = true, errorMessage = null)

        auth.createUserWithEmailAndPassword(state.email, state.password)
            .addOnSuccessListener { result ->
                val firebaseUser = result.user!!
                val uid = firebaseUser.uid
                val now = Timestamp.now()

                firebaseUser.sendEmailVerification()

                val newUser = User(
                    uid = uid,
                    email = state.email,
                    proveedor = "EMAIL",
                    fecha_creacion = now,
                    fecha_actualizacion = now,
                    esta_eliminado = false
                )

                firestore.collection("users")
                    .document(uid)
                    .set(newUser)
                    .addOnSuccessListener {
                        auth.signOut()
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isRegistered = true
                        )
                    }
                    .addOnFailureListener {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
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

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

}