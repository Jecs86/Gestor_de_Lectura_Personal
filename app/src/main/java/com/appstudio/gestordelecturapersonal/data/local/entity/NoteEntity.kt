package com.appstudio.gestordelecturapersonal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Relación con usuario y libro
    val uid: String = "",
    val bookId: Long = 0,

    // Página del libro
    val pagina: Int? = null,

    // Contenido de la nota
    val contenido: String = "",

    // Auditoría
    val fechaCreacion: Long = 0,
    val fechaActualizacion: Long = 0,

    // Soft delete
    val estaEliminado: Boolean = false,

    // sincronización
    @ColumnInfo(defaultValue = "0")
    val syncPending: Boolean = false
)
