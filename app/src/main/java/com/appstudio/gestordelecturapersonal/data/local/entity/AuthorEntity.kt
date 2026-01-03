package com.appstudio.gestordelecturapersonal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authors")
data class AuthorEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nombre: String = "",

    // Auditoría
    val fechaCreacion: Long = 0,
    val fechaActualizacion: Long = 0,

    // Soft delete
    val estaEliminado: Boolean = false,

    // sincronización
    @ColumnInfo(defaultValue = "0")
    val syncPending: Boolean = false
)
