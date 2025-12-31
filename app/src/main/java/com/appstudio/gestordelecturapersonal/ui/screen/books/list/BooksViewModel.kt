package com.appstudio.gestordelecturapersonal.ui.screen.books.list


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstudio.gestordelecturapersonal.data.local.dao.BookDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BooksViewModel(
    private val bookDao: BookDao
) : ViewModel() {

    private val _books = MutableStateFlow<List<BookUiModel>>(emptyList())
    val books : StateFlow<List<BookUiModel>> = _books

    init {
        loadBooks()
    }

    private fun loadBooks() {
        viewModelScope.launch {
            bookDao.obtenerLibrosUi()
                .collectLatest {
                    lista -> _books.value = lista
                }
        }
    }

}