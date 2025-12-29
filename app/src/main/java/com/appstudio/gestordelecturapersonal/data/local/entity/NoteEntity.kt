package com.appstudio.gestordelecturapersonal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Relación con usuario y libro
    val uid: String,
    val bookId: Long,

    // Página del libro
    val pagina: Int? = null,

    // Contenido de la nota
    val contenido: String,

    // Auditoría
    val fechaCreacion: Long,
    val fechaActualizacion: Long,

    // Soft delete
    val estaEliminado: Boolean = false
)
