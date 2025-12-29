package com.appstudio.gestordelecturapersonal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class GenreEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nombre: String,

    // Auditoría
    val fechaCreacion: Long,
    val fechaActualizacion: Long,

    // Soft delete
    val estaEliminado: Boolean = false
)
