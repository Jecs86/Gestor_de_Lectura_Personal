package com.appstudio.gestordelecturapersonal.ui.screen.books.list


data class BookUiModel(
    val id: Long,
    val titulo: String,
    val autor: String,
    val genero: String,
    val paginasLeidas: Int,
    val paginasTotales: Int,
    val estado: String, // leyendo | leido | pendiente
    val urlPortada: String?
) {
    val progreso: Int
        get() = (paginasLeidas * 100) / paginasTotales
}

data class BooksUiState(
    val libros: List<BookUiModel>,
    val searchQuery: String = ""
)
