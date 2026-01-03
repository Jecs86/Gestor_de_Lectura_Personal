package com.appstudio.gestordelecturapersonal.ui.screen.books.list

import com.appstudio.gestordelecturapersonal.data.local.entity.BookEntity

data class DeletedBookUiModel(
    val id: Long,
    val titulo: String,
    val urlPortada: String?
)

fun BookEntity.toDeletedUiModel(): DeletedBookUiModel {
    return DeletedBookUiModel(
        id = id,
        titulo = titulo,
        urlPortada = urlPortada
    )
}