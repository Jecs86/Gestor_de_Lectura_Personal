package com.appstudio.gestordelecturapersonal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Relación con usuario
    val uid: String,

    // Datos principales
    val titulo: String,

    // Normalización
    val authorId: Long,
    val genreId: Long,

    // Progreso de lectura
    val paginasTotales: Int,
    val paginasLeidas: Int,

    // Estado: pendiente | leyendo | completado
    val estado: String,

    // Portada (Firebase Storage)
    val urlPortada: String? = null,

    // Auditoría
    val fechaCreacion: Long,
    val fechaActualizacion: Long,

    // Soft delete
    val estaEliminado: Boolean = false
)