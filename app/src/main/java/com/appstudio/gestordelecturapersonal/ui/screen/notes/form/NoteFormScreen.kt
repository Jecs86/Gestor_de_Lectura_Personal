package com.appstudio.gestordelecturapersonal.ui.screen.notes.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.appstudio.gestordelecturapersonal.data.local.db.DatabaseProvider
import com.appstudio.gestordelecturapersonal.ui.component.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteFormScreen(
    navController: NavController,
    bookId: Long,
    noteId: Long? = null,
    onBackPage: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: NoteFormViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = DatabaseProvider.getDatabase(context)
                return NoteFormViewModel(
                    noteDao = database.noteDao()
                ) as T
            }
        }
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setBook(bookId)
        noteId?.let { viewModel.loadNote(it) }
    }

    val title: String = if (uiState.isEdit) "Editar nota" else "Agregar nota"

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
                minLines = 4
            )

            OutlinedTextField(
                value = uiState.pagina,
                onValueChange = viewModel::updatePagina,
                label = { Text("Página (opcional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = {
                    viewModel.saveNote()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(title)
            }
        }
    }
}