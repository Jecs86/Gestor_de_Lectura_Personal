package com.appstudio.gestordelecturapersonal.ui.screen.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appstudio.gestordelecturapersonal.data.local.dao.NoteDao
import com.appstudio.gestordelecturapersonal.data.local.dao.PendingDeleteDao
import com.appstudio.gestordelecturapersonal.data.repository.SyncManager

class NoteViewModelFactory (
    private val pendingDeleteDao: PendingDeleteDao,
    private val noteDao: NoteDao,
    private val bookId: Long,
    private val syncManager: SyncManager?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(
                pendingDeleteDao,
                noteDao,
                bookId,
                syncManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}