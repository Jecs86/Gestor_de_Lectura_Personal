package com.appstudio.gestordelecturapersonal.ui.screen.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.appstudio.gestordelecturapersonal.ui.navigation.AppRoutes

@Composable
fun NotesContent(
    modifier: Modifier,
    notes: List<NoteUiModel>,
    navController: NavController,
    bookId: Long,
    onEditNote: (noteId: Long) -> Unit
) {
    if (notes.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text("No hay notas para este libro")
        }
    } else {
        Text("Cantidad de notas: ${notes.size}")
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(notes) { note ->
                NoteItem(
                    note = note,
                    onLongPress = {
                        onEditNote(note.id)
                    }
                )
            }
        }
    }
}