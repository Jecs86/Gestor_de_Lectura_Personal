package com.appstudio.gestordelecturapersonal.ui.screen.books.list


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstudio.gestordelecturapersonal.data.local.dao.BookDao
import com.appstudio.gestordelecturapersonal.data.local.dao.NoteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BooksViewModel(
    private val bookDao: BookDao,
    private val noteDao: NoteDao
) : ViewModel() {

    private val _books = MutableStateFlow<List<BookUiModel>>(emptyList())
    val books : StateFlow<List<BookUiModel>> = _books

    private val _deletedBooks = MutableStateFlow<List<DeletedBookUiModel>>(emptyList())
    val deletedBooks: StateFlow<List<DeletedBookUiModel>> = _deletedBooks

    init {
        observeBooks()
        observeDeletedBooks()
    }

    private fun observeBooks() {
        viewModelScope.launch {
            bookDao.obtenerLibrosUi()
                .collectLatest {
                    lista -> _books.value = lista
                }
        }
    }

    fun softDeleteBook(bookId: Long) {
        viewModelScope.launch {
            bookDao.eliminarLogico(
                id = bookId,
                timestamp = System.currentTimeMillis()
            )
            observeBooks()
        }
    }

    private fun observeDeletedBooks() {
        viewModelScope.launch {
            bookDao.obtenerLibrosEliminados().collect { list ->
                _deletedBooks.value = list.map { it.toDeletedUiModel() }
            }
        }
    }

    fun restoreBook(bookId: Long) {
        viewModelScope.launch {
            bookDao.restaurarLibro(bookId, System.currentTimeMillis())
        }
    }

    fun deleteBookForever(bookId: Long) {
        viewModelScope.launch {
            noteDao.deleteNotesByBook(bookId)
            bookDao.eliminarLibroDefinitivo(bookId)
        }
    }

}