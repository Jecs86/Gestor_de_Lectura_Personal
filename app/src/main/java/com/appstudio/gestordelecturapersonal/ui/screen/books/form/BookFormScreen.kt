package com.appstudio.gestordelecturapersonal.ui.screen.books.form

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.appstudio.gestordelecturapersonal.data.local.db.DatabaseProvider
import com.appstudio.gestordelecturapersonal.ui.component.AppTopBar
import com.appstudio.gestordelecturapersonal.ui.component.DropdownSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookFormScreen(
    navController: NavController,
    bookId: Long? = null,
    onBackPage: () -> Unit
) {

    val context = LocalContext.current

    val database = DatabaseProvider.getDatabase(context)



    val viewModel: BookFormViewModel = viewModel(
        factory = BookFormViewModelFactory(
            database.bookDao(),
            database.authorDao(),
            database.genreDao(),
            context
        )
    )

    val uiState by viewModel.uiState.collectAsState()
    val authors by viewModel.authors.collectAsState()
    val genres by viewModel.genres.collectAsState()

    val estados = listOf("PENDIENTE", "LEYENDO", "COMPLETADO")

    // Cargar libro si es edición
    LaunchedEffect(bookId) {
        bookId?.let { viewModel.loadBook(it) }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.updatePortada(uri)
    }

    val title: String = if (uiState.isEdit) "Actualizar Libro" else "Guardar Libro"

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
                value = uiState.titulo,
                onValueChange = viewModel::updateTitulo,
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownSelector(
                label = "Autor",
                items = authors.map { it.nombre },
                selectedIndex = authors.indexOfFirst { it.id == uiState.autorId },
                onSelect = { viewModel.updateAutor(authors[it].id) },
                onAddNew = viewModel::addAuthor
            )

            DropdownSelector(
                label = "Género",
                items = genres.map { it.nombre },
                selectedIndex = genres.indexOfFirst { it.id == uiState.generoId },
                onSelect = { viewModel.updateGenero(genres[it].id) },
                onAddNew = viewModel::addGenre
            )

            DropdownSelector(
                label = "Estado",
                items = estados,
                selectedIndex = estados.indexOf(uiState.estado),
                onSelect = { viewModel.updateEstado(estados[it]) }
            )

            OutlinedTextField(
                value = uiState.paginasTotales,
                onValueChange = viewModel::updatePaginasTotales,
                label = { Text("Páginas totales") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = uiState.paginasLeidas,
                onValueChange = viewModel::updatePaginasLeidas,
                label = { Text("Páginas leídas") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(onClick = { imagePicker.launch("image/*") }) {
                Text("Seleccionar portada")
            }

            Button(
                onClick = {
                    viewModel.saveOrUpdateBook()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(title.dropLast(6))
            }
        }
    }
}
