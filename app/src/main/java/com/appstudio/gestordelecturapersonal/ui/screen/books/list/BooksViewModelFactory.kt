package com.appstudio.gestordelecturapersonal.ui.screen.books.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appstudio.gestordelecturapersonal.data.local.dao.BookDao

class BooksViewModelFactory(
    private val bookDao: BookDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BooksViewModel::class.java)) {
            return BooksViewModel(bookDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}