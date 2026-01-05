package com.appstudio.gestordelecturapersonal.ui.screen.books.form

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.appstudio.gestordelecturapersonal.data.local.db.DatabaseProvider
import com.appstudio.gestordelecturapersonal.data.repository.SyncManager
import com.appstudio.gestordelecturapersonal.ui.component.AppTopBar
import com.appstudio.gestordelecturapersonal.ui.component.CustomLoadingOverlay
import com.appstudio.gestordelecturapersonal.ui.component.DropdownSelector
import com.appstudio.gestordelecturapersonal.ui.component.NotificationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookFormScreen(
    navController: NavController,
    bookId: Long? = null,
    onBackPage: () -> Unit,
    syncManager: SyncManager?,
    onShowSnackbar: (String, NotificationType) -> Unit
) {

    val context = LocalContext.current

    val database = DatabaseProvider.getDatabase(context)



    val viewModel: BookFormViewModel = viewModel(
        factory = BookFormViewModelFactory(
            database.bookDao(),
            database.authorDao(),
            database.genreDao(),
            syncManager
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

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            val message = if (bookId == null) "Libro guardado con éxito" else "Libro actualizado"
            onShowSnackbar(message, NotificationType.Saved)
            onBackPage()
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.updatePortada(uri)
    }

    val title: String = if (uiState.isEdit) "Actualizar Libro" else "Guardar Libro"

    val focusManager = LocalFocusManager.current


    Box(modifier = Modifier.fillMaxSize()){
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
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions( onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )

                DropdownSelector(
                    label = "Autor",
                    items = authors.map { it.nombre },
                    selectedIndex = authors.indexOfFirst { it.id == uiState.autorId },
                    onSelect = { viewModel.updateAutor(authors[it].id) },
                    onAddNew = viewModel::addAuthor,
                    enabled = !uiState.isLoading
                )

                DropdownSelector(
                    label = "Género",
                    items = genres.map { it.nombre },
                    selectedIndex = genres.indexOfFirst { it.id == uiState.generoId },
                    onSelect = { viewModel.updateGenero(genres[it].id) },
                    onAddNew = viewModel::addGenre,
                    enabled = !uiState.isLoading
                )

                DropdownSelector(
                    label = "Estado",
                    items = estados,
                    selectedIndex = estados.indexOf(uiState.estado),
                    onSelect = { viewModel.updateEstado(estados[it]) },
                    enabled = !uiState.isLoading
                )

                OutlinedTextField(
                    value = uiState.paginasTotales,
                    onValueChange = viewModel::updatePaginasTotales,
                    label = { Text("Páginas totales") },
                    enabled = !uiState.isLoading,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions( onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )

                OutlinedTextField(
                    value = uiState.paginasLeidas,
                    onValueChange = viewModel::updatePaginasLeidas,
                    label = { Text("Páginas leídas") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions( onNext = {
                        focusManager.clearFocus()
                    }),
                    enabled = !uiState.isLoading && uiState.estado == "LEYENDO"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    Button(
                        onClick = { imagePicker.launch("image/*") },
                        enabled = !uiState.isLoading
                    ) {
                        Text("Seleccionar portada (opcional)")
                    }

                    // Vista previa de la imagen
                    if (uiState.portadaUri != null) {
                        AsyncImage(
                            model = uiState.portadaUri,
                            contentDescription = "Vista previa",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Image, contentDescription = null, tint = Color.Gray)
                        }
                    }

                }

                Button(
                    onClick = {
                        viewModel.validateAndSave { error ->
                            onShowSnackbar(error, NotificationType.Offline)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                ) {
                    Text(title.dropLast(6))
                }
            }
        }

        CustomLoadingOverlay(
            isLoading = uiState.isLoading,
            message = if (uiState.isEdit) "Actualizando libro..." else "Guardando nuevo libro..."
        )

    }


}
