package com.appstudio.gestordelecturapersonal.ui.screen.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstudio.gestordelecturapersonal.data.local.dao.NoteDao
import com.appstudio.gestordelecturapersonal.data.local.dao.PendingDeleteDao
import com.appstudio.gestordelecturapersonal.data.local.entity.PendingDeleteEntity
import com.appstudio.gestordelecturapersonal.data.repository.SyncManager
import com.appstudio.gestordelecturapersonal.ui.screen.notes.NoteUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel(
    private val pendingDeleteDao: PendingDeleteDao,
    private val noteDao: NoteDao,
    private val bookId: Long,
    private val syncManager: SyncManager?
) : ViewModel() {

    private val _notes = MutableStateFlow<List<NoteUiModel>>(emptyList())
    private val _deletedNotes = MutableStateFlow<List<NoteUiModel>>(emptyList())
    val notes: StateFlow<List<NoteUiModel>> = _notes
    val deletedNotes: StateFlow<List<NoteUiModel>> = _deletedNotes

    init {
        observeNotes()
        observeDeletedNotes()
    }

    private fun observeNotes() {
        viewModelScope.launch {
            noteDao.obtenerNotas(bookId).collect { list ->
                _notes.value = list.map { it.toUiModel() }
            }
        }
    }

    private fun observeDeletedNotes() {
        viewModelScope.launch {
            noteDao.getDeletedNotesByBook(bookId).collect { list ->
                _deletedNotes.value = list.map { it.toUiModel() }
            }
        }
    }

    fun softDeleteNote(noteId: Long) {
        viewModelScope.launch {
            noteDao.softDeleteNote(
                noteId = noteId
            )
            noteDao.markSyncPending(noteId)
            syncManager?.notifyChange()
        }
    }

    fun restoreNote(noteId: Long) {
        viewModelScope.launch {
            noteDao.restoreNote(noteId)
            noteDao.markSyncPending(noteId)
            syncManager?.notifyChange()
        }
    }

    fun deleteNoteForever(noteId: Long, bookId: Long) {
        viewModelScope.launch {

            pendingDeleteDao.insert(
                PendingDeleteEntity(
                    entityType = "note",
                    entityId = noteId,
                    parentId = bookId
                )
            )

            noteDao.deleteNoteForever(noteId)
            syncManager?.notifyChange()
        }
    }
}