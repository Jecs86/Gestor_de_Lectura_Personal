package com.appstudio.gestordelecturapersonal.ui.screen.books.form

import android.net.Uri

data class BookFormUiState(
    val id: Long? = null,
    val uid: String? = null,
    val titulo: String = "",
    val autorId: Long? = null,
    val generoId: Long? = null,
    val estado: String = "",
    val paginasTotales: String = "",
    val paginasLeidas: String = "",
    val portadaUri: Uri? = null,
    val isEdit: Boolean = false
)