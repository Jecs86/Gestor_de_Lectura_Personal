package com.appstudio.gestordelecturapersonal.ui.screen.books


data class BookUiModel(
    val titulo: String,
    val autor: String,
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
