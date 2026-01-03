package com.appstudio.gestordelecturapersonal.ui.screen.books.form

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appstudio.gestordelecturapersonal.data.local.dao.AuthorDao
import com.appstudio.gestordelecturapersonal.data.local.dao.BookDao
import com.appstudio.gestordelecturapersonal.data.local.dao.GenreDao
import com.appstudio.gestordelecturapersonal.data.repository.SyncManager

class BookFormViewModelFactory (
    private val bookDao: BookDao,
    private val authorDao: AuthorDao,
    private val genreDao: GenreDao,
    private val syncManager: SyncManager?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookFormViewModel::class.java)) {
            return BookFormViewModel(
                bookDao,
                authorDao,
                genreDao,
                syncManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}