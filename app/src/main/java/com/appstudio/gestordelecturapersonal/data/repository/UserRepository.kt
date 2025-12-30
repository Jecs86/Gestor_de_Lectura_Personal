package com.appstudio.gestordelecturapersonal.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.appstudio.gestordelecturapersonal.data.model.User

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val usersCollection = firestore.collection("users")

    fun saveUserIfNotExists(
        proveedor: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onComplete(false, "Usuario no autenticado")
            return
        }

        val uid = currentUser.uid
        val email = currentUser.email ?: ""

        val userRef = usersCollection.document(uid)

        userRef.get()
            .addOnSuccessListener { document ->
                val now = Timestamp.now()

                if (document.exists()) {
                    // Usuario ya existe → solo actualizar
                    userRef.update(
                        mapOf(
                            "fecha_actualizacion" to now,
                            "esta_eliminado" to false
                        )
                    )
                    onComplete(true, null)
                } else {
                    // Usuario nuevo
                    val newUser = User(
                        uid = uid,
                        email = email,
                        proveedor = proveedor,
                        fecha_creacion = now,
                        fecha_actualizacion = now,
                        esta_eliminado = false
                    )

                    userRef.set(newUser)
                        .addOnSuccessListener {
                            onComplete(true, null)
                        }
                        .addOnFailureListener {
                            onComplete(false, it.message)
                        }
                }
            }
            .addOnFailureListener {
                onComplete(false, it.message)
            }
    }
}
