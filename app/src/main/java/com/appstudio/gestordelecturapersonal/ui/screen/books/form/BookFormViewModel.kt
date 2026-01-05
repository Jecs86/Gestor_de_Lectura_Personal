package com.appstudio.gestordelecturapersonal.ui.screen.books.form

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstudio.gestordelecturapersonal.data.local.dao.AuthorDao
import com.appstudio.gestordelecturapersonal.data.local.dao.BookDao
import com.appstudio.gestordelecturapersonal.data.local.dao.GenreDao
import com.appstudio.gestordelecturapersonal.data.local.entity.AuthorEntity
import com.appstudio.gestordelecturapersonal.data.local.entity.BookEntity
import com.appstudio.gestordelecturapersonal.data.local.entity.GenreEntity
import com.appstudio.gestordelecturapersonal.data.repository.SyncManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BookFormViewModel(
    private val bookDao: BookDao,
    private val authorDao: AuthorDao,
    private val genreDao: GenreDao,
    private val syncManager: SyncManager?
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookFormUiState())
    val uiState: StateFlow<BookFormUiState> = _uiState

    private val _authors = MutableStateFlow<List<AuthorEntity>>(emptyList())
    val authors: StateFlow<List<AuthorEntity>> = _authors

    private val _genres = MutableStateFlow<List<GenreEntity>>(emptyList())
    val genres: StateFlow<List<GenreEntity>> = _genres

    init {
        loadAuthors()
        loadGenres()
    }

    private fun loadAuthors() {
        viewModelScope.launch {
            authorDao.obtenerAutores().collect { lista ->
                _authors.value = lista
            }
        }
    }

    private fun loadGenres() {
        viewModelScope.launch {
            genreDao.obtenerGeneros().collect {
                lista -> _genres.value = lista
            }
        }
    }

    fun addAuthor(nombre: String) {
        viewModelScope.launch {
            authorDao.insertar(AuthorEntity(
                nombre = nombre,
                fechaCreacion = System.currentTimeMillis(),
                fechaActualizacion = System.currentTimeMillis(),
                syncPending = true
            ))
            syncManager?.notifyChange()
        }
    }

    fun addGenre(nombre: String) {
        viewModelScope.launch {
            genreDao.insertar(GenreEntity(
                nombre = nombre,
                fechaCreacion = System.currentTimeMillis(),
                fechaActualizacion = System.currentTimeMillis(),
                syncPending = true
            ))
            syncManager?.notifyChange()
        }
    }

    fun loadBook(bookId: Long) {
        viewModelScope.launch {
            val book = bookDao.obtenerPorUid(bookId) ?: return@launch

            _uiState.value = BookFormUiState(
                id = book.id,
                uid = book.uid,
                titulo = book.titulo,
                autorId = book.authorId,
                generoId = book.genreId,
                estado = book.estado,
                paginasTotales = book.paginasTotales.toString(),
                paginasLeidas = book.paginasLeidas.toString(),
                portadaUri = book.urlPortada?.let { Uri.parse(it) },
                isEdit = true
            )
        }
    }

    fun saveOrUpdateBook() {
        val state = _uiState.value
        if (state.autorId == null || state.generoId == null) return
        if (state.titulo.isBlank()) return
        if (state.paginasTotales.isBlank()) return
        if (state.paginasLeidas.isBlank()) return

        _uiState.value = _uiState.value.copy(isLoading = true)

        Log.d("SYNC", "Iniciando Guardar/Actualizar Libro")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var finalCoverUrl: String? = state.portadaUri?.toString()

                if (state.portadaUri != null && state.portadaUri.toString().startsWith("content://")) {

                    Log.d("SYNC", "Intentando subir imagen: ${state.portadaUri}")

                    val uid = FirebaseAuth.getInstance().currentUser!!.uid
                    val bookId = state.id ?: System.currentTimeMillis()

                    val storageRef = FirebaseStorage.getInstance()
                        .reference
                        .child("book_covers/$uid/$bookId.jpg")

                    storageRef.putFile(state.portadaUri).await()

                    Log.d("SYNC", "Imagen subida, obteniendo URL...")

                    finalCoverUrl = storageRef.downloadUrl.await().toString()

                    Log.d("SYNC", "URL obtenida: $finalCoverUrl")
                }

                val now = System.currentTimeMillis()

                val book = BookEntity(
                    id = state.id ?: 0L,
                    uid = state.uid ?: FirebaseAuth.getInstance().currentUser!!.uid,
                    titulo = state.titulo,
                    authorId = state.autorId,
                    genreId = state.generoId,
                    estado = state.estado,
                    paginasTotales = state.paginasTotales.toInt(),
                    paginasLeidas = state.paginasLeidas.toInt(),
                    urlPortada = finalCoverUrl,
                    fechaCreacion = now,
                    fechaActualizacion = now,
                    estaEliminado = false,
                    syncPending = true
                )

                if (state.isEdit) {
                    bookDao.actualizar(book)
                } else {
                    bookDao.insertar(book)
                }
                syncManager?.notifyChange()
                Log.d("SYNC", "Teminando Guardar/Actualizar Libro")

                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSaved = true
                    )
                }


            }catch (e: Exception) {
                Log.e("SYNC", "Error al sincronizar con Firestore al guardar", e)
                withContext(Dispatchers.Main){
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    fun updateTitulo(value: String) {
        _uiState.value = _uiState.value.copy(titulo = value)
    }

    fun updateAutor(id: Long) {
        _uiState.value = _uiState.value.copy(autorId = id)
    }

    fun updateGenero(id: Long) {
        _uiState.value = _uiState.value.copy(generoId = id)
    }

    fun updateEstado(nuevoEstado: String) {

        val estadoActual = _uiState.value
        var nuevasPaginasLeidas = estadoActual.paginasLeidas

        when (nuevoEstado) {
            "PENDIENTE" -> {
                nuevasPaginasLeidas = "0"
            }
            "COMPLETADO" -> {
                nuevasPaginasLeidas = estadoActual.paginasTotales
            }
        }

        _uiState.value = estadoActual.copy(
            estado = nuevoEstado,
            paginasLeidas = nuevasPaginasLeidas
        )
    }

    fun updatePaginasTotales(value: String) {
        if (value.all(Char::isDigit)) {

            val estadoActual = _uiState.value

            val nuevasPaginasLeidas = if (estadoActual.estado == "COMPLETADO") value
                                else estadoActual.paginasLeidas

            _uiState.value = estadoActual.copy(
                paginasTotales = value,
                paginasLeidas = nuevasPaginasLeidas
            )
        }
    }

    fun updatePaginasLeidas(value: String) {

        if (_uiState.value.estado == "LEYENDO") {
            val total = _uiState.value.paginasTotales.toIntOrNull() ?: 0
            val leidas = value.toIntOrNull() ?: 0

            if (value.all(Char::isDigit) && leidas <= total) {
                _uiState.value = _uiState.value.copy(paginasLeidas = value)
            }
        }

    }

    fun updatePortada(uri: Uri?) {
        _uiState.value = _uiState.value.copy(portadaUri = uri)
    }

    fun validateAndSave(onValidationError: (String) -> Unit) {
        val state = _uiState.value

        val errorMsg = when {
            state.titulo.isBlank() -> "El título no puede estar vacío"
            state.autorId == null -> "Selecciona un autor"
            state.generoId == null -> "Selecciona un género"
            state.paginasTotales.isBlank() || state.paginasTotales.toInt() <= 0 -> "Las páginas totales deben ser mayor a 0"
            state.paginasLeidas.isBlank() -> "Las páginas leídas no pueden estar vacías"
            else -> null
        }

        if (errorMsg != null) {
            onValidationError(errorMsg)
            return
        }

        saveOrUpdateBook()
    }

}