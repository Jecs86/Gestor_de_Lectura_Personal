package com.appstudio.gestordelecturapersonal.ui.screen.books.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appstudio.gestordelecturapersonal.data.local.dao.BookDao
import com.appstudio.gestordelecturapersonal.data.local.dao.NoteDao
import com.appstudio.gestordelecturapersonal.data.local.dao.PendingDeleteDao
import com.appstudio.gestordelecturapersonal.data.repository.SyncManager

class BooksViewModelFactory(
    private val pendingDeleteDao: PendingDeleteDao,
    private val bookDao: BookDao,
    private val noteDao: NoteDao,
    private val syncManager: SyncManager?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BooksViewModel::class.java)) {
            return BooksViewModel(
                pendingDeleteDao,
                bookDao,
                noteDao,
                syncManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}