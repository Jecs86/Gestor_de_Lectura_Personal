package com.appstudio.gestordelecturapersonal.data.auth

import com.google.firebase.auth.FirebaseAuth

object AuthSessionManager {

    fun hasValidSession(): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        return user != null && user.isEmailVerified
    }
}