package com.appstudio.gestordelecturapersonal.ui.screen.books.form

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstudio.gestordelecturapersonal.data.local.dao.AuthorDao
import com.appstudio.gestordelecturapersonal.data.local.dao.BookDao
import com.appstudio.gestordelecturapersonal.data.local.dao.GenreDao
import com.appstudio.gestordelecturapersonal.data.local.entity.AuthorEntity
import com.appstudio.gestordelecturapersonal.data.local.entity.BookEntity
import com.appstudio.gestordelecturapersonal.data.local.entity.GenreEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookFormViewModel(
    private val bookDao: BookDao,
    private val authorDao: AuthorDao,
    private val genreDao: GenreDao
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
                fechaActualizacion = System.currentTimeMillis()
            ))
        }
    }

    fun addGenre(nombre: String) {
        viewModelScope.launch {
            genreDao.insertar(GenreEntity(
                nombre = nombre,
                fechaCreacion = System.currentTimeMillis(),
                fechaActualizacion = System.currentTimeMillis()
            ))
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
    //TODO RESOLVER FALLO NULO
    fun saveOrUpdateBook() {
        val state = _uiState.value
        val now = System.currentTimeMillis()

        val book = BookEntity(
            id = state.id ?: 0L,
            uid = state.uid ?: FirebaseAuth.getInstance().currentUser!!.uid,
            titulo = state.titulo,
            authorId = state.autorId!!,
            genreId = state.generoId!!,
            estado = state.estado,
            paginasTotales = state.paginasTotales.toInt(),
            paginasLeidas = state.paginasLeidas.toInt(),
            urlPortada = state.portadaUri?.toString(),
            fechaCreacion = now,
            fechaActualizacion = now,
            estaEliminado = false
        )

        viewModelScope.launch {
            if (state.isEdit) {
                bookDao.actualizar(book)
            } else {
                bookDao.insertar(book)
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

    fun updateEstado(value: String) {
        _uiState.value = _uiState.value.copy(estado = value)
    }

    fun updatePaginasTotales(value: String) {
        if (value.all(Char::isDigit)) {
            _uiState.value = _uiState.value.copy(paginasTotales = value)
        }
    }

    fun updatePaginasLeidas(value: String) {
        val total = _uiState.value.paginasTotales.toIntOrNull() ?: 0
        val leidas = value.toIntOrNull() ?: 0

        if (value.all(Char::isDigit) && leidas <= total) {
            _uiState.value = _uiState.value.copy(paginasLeidas = value)
        }
    }

    fun updatePortada(uri: Uri?) {
        _uiState.value = _uiState.value.copy(portadaUri = uri)
    }

}