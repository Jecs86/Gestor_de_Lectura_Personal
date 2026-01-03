package com.appstudio.gestordelecturapersonal.ui.screen.notes

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.appstudio.gestordelecturapersonal.data.local.db.DatabaseProvider
import com.appstudio.gestordelecturapersonal.data.repository.SyncManager
import com.appstudio.gestordelecturapersonal.ui.component.AppTopBar
import com.appstudio.gestordelecturapersonal.ui.navigation.AppRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navController: NavController,
    bookId: Long,
    onBackPage: () -> Unit,
    syncManager: SyncManager?
) {

    val context = LocalContext.current

    val viewModel: NotesViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = DatabaseProvider.getDatabase(context)
                return NotesViewModel(
                    noteDao = database.noteDao(),
                    bookId = bookId,
                    pendingDeleteDao = database.pendingDeleteDao(),
                    syncManager = syncManager
                ) as T
            }
        }
    )

    val notes by viewModel.notes.collectAsState()

    var showTrash by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Notas",
                needBackPage = true,
                onBackPage = onBackPage
            )
        },
        floatingActionButton = {
            FloatingActionButton( onClick = { showTrash = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Papelera"
                )
            }
        }
    ) { padding ->
        NotesContent(
            modifier = Modifier.padding(padding),
            notes = notes,
            navController = navController,
            bookId = bookId,
            onEditNote = { noteId ->
                navController.navigate(
                    AppRoutes.EditNote.createRoute(bookId, noteId)
                )
            },
            onDeleteNote = {noteId ->
                viewModel.softDeleteNote(noteId)
            }
        )

        if (showTrash) {
            NotesTrashBottomSheet(
                notes = viewModel.deletedNotes.collectAsState().value,
                onRestore = { viewModel.restoreNote(it) },
                onDeleteForever = { viewModel.deleteNoteForever(it, bookId) },
                onDismiss = { showTrash = false }
            )
        }
    }
}