package com.appstudio.gestordelecturapersonal.ui.screen.notes.form

data class NoteFormUiState(
    val id: Long? = null,
    val bookId: Long? = null,
    val uid: String? = null,
    val contenido: String = "",
    val pagina: String = "",
    val maxPagina: Int = Int.MAX_VALUE,
    val errorMessage: String? = null,
    val isEdit: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)