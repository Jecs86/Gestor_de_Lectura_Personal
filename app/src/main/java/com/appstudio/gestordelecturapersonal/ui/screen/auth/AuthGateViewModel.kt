package com.appstudio.gestordelecturapersonal.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthGateViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _isAuthenticated = MutableStateFlow<Boolean?>(null)
    val isAuthenticated: StateFlow<Boolean?> = _isAuthenticated

    init {
        viewModelScope.launch {
            delay(1500)
            auth.addAuthStateListener { firebaseAuth ->
                val user = firebaseAuth.currentUser
                _isAuthenticated.value = user != null && user.isEmailVerified
            }
        }
    }
}