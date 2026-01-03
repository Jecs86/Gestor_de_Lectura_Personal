package com.appstudio.gestordelecturapersonal.ui.screen.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstudio.gestordelecturapersonal.data.local.dao.NoteDao
import com.appstudio.gestordelecturapersonal.ui.screen.notes.NoteUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel(
    private val noteDao: NoteDao,
    private val bookId: Long
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
        }
    }

    fun restoreNote(noteId: Long) {
        viewModelScope.launch {
            noteDao.restoreNote(noteId)
        }
    }

    fun deleteNoteForever(noteId: Long) {
        viewModelScope.launch {
            noteDao.deleteNoteForever(noteId)
        }
    }
}