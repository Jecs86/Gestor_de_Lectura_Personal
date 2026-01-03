package com.appstudio.gestordelecturapersonal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Relación con usuario
    val uid: String = "",

    // Datos principales
    val titulo: String = "",

    // Normalización
    val authorId: Long = 0,
    val genreId: Long = 0,

    // Progreso de lectura
    val paginasTotales: Int = 0,
    val paginasLeidas: Int = 0,

    // Estado: pendiente | leyendo | completado
    val estado: String = "",

    // Portada (Firebase Storage)
    val urlPortada: String? = null,

    // Auditoría
    val fechaCreacion: Long = 0,
    val fechaActualizacion: Long = 0,

    // Soft delete
    val estaEliminado: Boolean = false,

    // sincronización
    @ColumnInfo(defaultValue = "0")
    val syncPending: Boolean = false
)