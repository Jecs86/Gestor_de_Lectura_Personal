package com.appstudio.gestordelecturapersonal.ui.screen.notes

import com.appstudio.gestordelecturapersonal.data.local.entity.NoteEntity

data class NoteUiModel(
    val id: Long,
    val pagina: Int?,
    val contenido: String,
    val fechaActualizacion: Long
)

fun NoteEntity.toUiModel(): NoteUiModel {
    return NoteUiModel(
        id = id,
        pagina = pagina,
        contenido = contenido,
        fechaActualizacion = fechaActualizacion
    )
}