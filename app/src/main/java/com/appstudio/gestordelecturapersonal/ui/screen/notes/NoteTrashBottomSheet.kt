package com.appstudio.gestordelecturapersonal.ui.screen.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.appstudio.gestordelecturapersonal.ui.component.DeleteDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTrashBottomSheet(
    notes: List<NoteUiModel>,
    onRestore: (Long) -> Unit,
    onDeleteForever: (Long) -> Unit,
    onDismiss: () -> Unit
) {

    var selectedNote by remember { mutableStateOf<NoteUiModel?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = "Papelera de notas",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (notes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("La papelera está vacía")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(notes) { note ->
                        TrashNoteItem(
                            note = note,
                            onRestore = { onRestore(note.id) },
                            onDeleteForever = {
                                selectedNote = note
                                showDeleteDialog = true
                            }
                        )
                    }
                }

                if (showDeleteDialog && selectedNote != null) {
                    DeleteDialog(
                        element = selectedNote!!.contenido,
                        elementTitle = "Nota",
                        onConfirm = {
                            onDeleteForever(selectedNote!!.id)
                            selectedNote = null
                            showDeleteDialog = false
                            onDismiss()
                        },
                        onDismiss = {
                            showDeleteDialog = false
                        },
                        isPermanent = true
                    )
                }

            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}