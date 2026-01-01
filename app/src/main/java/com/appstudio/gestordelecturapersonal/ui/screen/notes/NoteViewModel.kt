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
    val notes: StateFlow<List<NoteUiModel>> = _notes

    init {
        observeNotes()
    }

    private fun observeNotes() {
        viewModelScope.launch {
            noteDao.obtenerNotas(bookId).collect { list ->
                _notes.value = list.map { it.toUiModel() }
            }
        }
    }
}