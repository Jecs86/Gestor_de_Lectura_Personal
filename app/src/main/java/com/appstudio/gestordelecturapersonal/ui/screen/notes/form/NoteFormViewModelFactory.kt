package com.appstudio.gestordelecturapersonal.ui.screen.notes.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appstudio.gestordelecturapersonal.data.local.dao.BookDao
import com.appstudio.gestordelecturapersonal.data.local.dao.NoteDao
import com.appstudio.gestordelecturapersonal.data.repository.SyncManager

class NoteFormViewModelFactory (
    private val noteDao: NoteDao,
    private val bookDao: BookDao,
    private val syncManager: SyncManager?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteFormViewModel::class.java)) {
            return NoteFormViewModel(
                noteDao,
                bookDao,
                syncManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}