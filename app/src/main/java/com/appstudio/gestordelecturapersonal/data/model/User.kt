package com.appstudio.gestordelecturapersonal.data.model

import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val email: String = "",
    val proveedor: String = "",
    val fecha_creacion: Timestamp? = null,
    val fecha_actualizacion: Timestamp? = null,
    val esta_eliminado: Boolean = false
)