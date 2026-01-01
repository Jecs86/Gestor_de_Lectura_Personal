package com.appstudio.gestordelecturapersonal.ui.screen.notes.form

data class NoteFormUiState(
    val id: Long? = null,
    val bookId: Long? = null,
    val uid: String? = null,

    val contenido: String = "",
    val pagina: String = "",

    val isEdit: Boolean = false
)