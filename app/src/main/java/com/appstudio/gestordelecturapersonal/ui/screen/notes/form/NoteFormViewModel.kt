package com.appstudio.gestordelecturapersonal.ui.screen.notes.form

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstudio.gestordelecturapersonal.data.local.dao.BookDao
import com.appstudio.gestordelecturapersonal.data.local.dao.NoteDao
import com.appstudio.gestordelecturapersonal.data.local.entity.NoteEntity
import com.appstudio.gestordelecturapersonal.data.repository.SyncManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteFormViewModel(
    private val noteDao: NoteDao,
    private val bookDao: BookDao,
    private val syncManager: SyncManager?
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

    fun loadBookInfo(bookId: Long) {
        viewModelScope.launch {
            val book = bookDao.obtenerPorUid(bookId)
            _uiState.value = _uiState.value.copy(maxPagina = book?.paginasTotales ?: Int.MAX_VALUE)
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

    fun saveNote(onValidationError: (String) -> Unit) {
        val state = _uiState.value
        val now = System.currentTimeMillis()


        if (state.contenido.isBlank()) {
            onValidationError("El contenido de la nota no puede estar vacío")
            return
        }

        val paginaActual = state.pagina.toIntOrNull()
        Log.d("SYNC", "Página actual: $paginaActual, Máxima página: ${state.maxPagina}")
        if (paginaActual != null && paginaActual > state.maxPagina) {
            onValidationError("La página ($paginaActual) no puede ser mayor al total de páginas del libro (${state.maxPagina})")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val note = NoteEntity(
                    id = state.id ?: 0L,
                    bookId = state.bookId!!,
                    uid = state.uid ?: FirebaseAuth.getInstance().currentUser!!.uid,
                    contenido = state.contenido,
                    pagina = state.pagina.toIntOrNull(),
                    fechaCreacion = now,
                    fechaActualizacion = now,
                    estaEliminado = false,
                    syncPending = true
                )

                if (state.isEdit) {
                    noteDao.actualizar(note)
                } else {
                    noteDao.insertar(note)
                }
                syncManager?.notifyChange()

                Log.d("SYNC", "Teminando Guardar/Actualizar Nota")

                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSaved = true
                    )
                }

            }catch (e: Exception) {
                Log.e("SYNC", "Error al sincronizar con Firestore al guardar", e)
                withContext(Dispatchers.Main){
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "Error en la sincronizacion nota")
                }
            }
        }
    }
}
