package com.appstudio.gestordelecturapersonal.ui.screen.notes.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.appstudio.gestordelecturapersonal.data.local.db.DatabaseProvider
import com.appstudio.gestordelecturapersonal.data.repository.SyncManager
import com.appstudio.gestordelecturapersonal.ui.component.AppTopBar
import com.appstudio.gestordelecturapersonal.ui.component.CustomLoadingOverlay
import com.appstudio.gestordelecturapersonal.ui.component.NotificationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteFormScreen(
    navController: NavController,
    bookId: Long,
    noteId: Long? = null,
    onBackPage: () -> Unit,
    syncManager: SyncManager?,
    onShowSnackbar: (String, NotificationType) -> Unit
) {
    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)

    val viewModel: NoteFormViewModel = viewModel(
        factory = NoteFormViewModelFactory(
            noteDao = database.noteDao(),
            bookDao = database.bookDao(),
            syncManager = syncManager
        )
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setBook(bookId)
        viewModel.loadBookInfo(bookId)
        noteId?.let { viewModel.loadNote(it) }
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            val message = if (noteId == null) "Nota agregada" else "Nota actualizada"
            onShowSnackbar(message, NotificationType.Saved)
            onBackPage()
        }
    }

    val title: String = if (uiState.isEdit) "Editar nota" else "Agregar nota"

    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                AppTopBar(
                    title = title,
                    needBackPage = true,
                    onBackPage = onBackPage
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedTextField(
                    value = uiState.contenido,
                    onValueChange = viewModel::updateContenido,
                    label = { Text("Contenido de la nota") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    enabled = !uiState.isLoading
                )

                OutlinedTextField(
                    value = uiState.pagina,
                    onValueChange = viewModel::updatePagina,
                    label = { Text("Página (opcional)") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions( onDone = {
                        focusManager.clearFocus()
                    }),
                    enabled = !uiState.isLoading
                )

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.saveNote(onValidationError = {error ->
                            onShowSnackbar(error, NotificationType.Offline)
                        })
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                ) {
                    Text(title.dropLast(5))
                }
            }
        }

        CustomLoadingOverlay(
            isLoading = uiState.isLoading,
            message = if (uiState.isEdit) "Actualizando Nota..." else "Guardando nueva Nota..."
        )
    }
}