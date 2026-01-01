package com.appstudio.gestordelecturapersonal.ui.screen.notes.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstudio.gestordelecturapersonal.data.local.dao.NoteDao
import com.appstudio.gestordelecturapersonal.data.local.entity.NoteEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteFormViewModel(
    private val noteDao: NoteDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteFormUiState())
    val uiState: StateFlow<NoteFormUiState> = _uiState

    fun loadNote(noteId: Long) {
        viewModelScope.launch {
            val note = noteDao.obtenerPorId(noteId)
            _uiState.value = NoteFormUiState(
                id = note.id,
                bookId = note.bookId,
                uid = note.uid,
                contenido = note.contenido,
                pagina = note.pagina?.toString() ?: "",
                isEdit = true
            )
        }
    }

    fun setBook(bookId: Long) {
        _uiState.value = _uiState.value.copy(bookId = bookId)
    }

    fun updateContenido(value: String) {
        _uiState.value = _uiState.value.copy(contenido = value)
    }

    fun updatePagina(value: String) {
        if (value.all(Char::isDigit)) {
            _uiState.value = _uiState.value.copy(pagina = value)
        }
    }

    fun saveNote() {
        val state = _uiState.value
        val now = System.currentTimeMillis()

        val note = NoteEntity(
            id = state.id ?: 0L,
            bookId = state.bookId!!,
            uid = state.uid ?: FirebaseAuth.getInstance().currentUser!!.uid,
            contenido = state.contenido,
            pagina = state.pagina.toIntOrNull(),
            fechaCreacion = now,
            fechaActualizacion = now,
            estaEliminado = false
        )

        viewModelScope.launch {
            if (state.isEdit) {
                noteDao.actualizar(note)
            } else {
                noteDao.insertar(note)
            }
        }
    }
}
