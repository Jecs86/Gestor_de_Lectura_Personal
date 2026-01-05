package com.appstudio.gestordelecturapersonal.ui.screen.books.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.appstudio.gestordelecturapersonal.data.local.db.DatabaseProvider
import com.appstudio.gestordelecturapersonal.data.repository.SyncManager
import com.appstudio.gestordelecturapersonal.ui.component.AppBottomBar
import com.appstudio.gestordelecturapersonal.ui.component.AppTopBar
import com.appstudio.gestordelecturapersonal.ui.component.BookOptionsBottomSheet
import com.appstudio.gestordelecturapersonal.ui.component.DeleteDialog
import com.appstudio.gestordelecturapersonal.ui.component.NotificationType
import com.appstudio.gestordelecturapersonal.ui.navigation.AppRoutes


@Composable
fun BooksScreen(
    navController: NavController,
    syncManager: SyncManager?,
    onShowSnackbar: (String, NotificationType) -> Unit
) {

    val context = LocalContext.current

    val database = DatabaseProvider.getDatabase(context)

    val viewModel: BooksViewModel = viewModel(
        factory = BooksViewModelFactory(
            database.pendingDeleteDao(),
            database.bookDao(),
            database.noteDao(),
            syncManager
        )
    )

    var selectedBook by remember { mutableStateOf<BookUiModel?>(null) }

    val libros by viewModel.books.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }

    var showTrash by remember { mutableStateOf(false) }
    val deletedBooks by viewModel.deletedBooks.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Libros"
            )
        },
        floatingActionButton = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FloatingActionButton(
                    onClick = {
                        showTrash = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Papelera"
                    )
                }

                FloatingActionButton(
                    onClick = {
                        navController.navigate("add_book")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar libro"
                    )
                }
            }
        },
        bottomBar = {
            AppBottomBar(navController)
        }
    ) { padding ->
        BooksContent(
            modifier = Modifier.padding(padding),
            libros = libros,
            onBookClick = { book ->
                selectedBook = book
            }
        )

        selectedBook?.let { book ->
            BookOptionsBottomSheet(
                book = book,
                onDismiss = { selectedBook = null },
                onEdit = {
                    selectedBook = null
                    navController.navigate("edit_book/${book.id}")
                },
                onDelete = {
                    showDeleteDialog = true
                },
                onViewNotes = {
                    selectedBook = null
                    navController.navigate(
                        AppRoutes.Notes.createRoute(book.id)
                    )
                }
            )
        }

        if (showDeleteDialog && selectedBook != null) {
            DeleteDialog(
                element = selectedBook!!.titulo,
                elementTitle = "Libro",
                onConfirm = {
                    viewModel.softDeleteBook(selectedBook!!.id)

                    onShowSnackbar("${selectedBook!!.titulo} movido a la papelera", NotificationType.Deleted)

                    selectedBook = null
                    showDeleteDialog = false
                },
                onDismiss = {
                    showDeleteDialog = false
                }
            )
        }

        if (showTrash) {
            BooksTrashBottomSheet(
                books = deletedBooks,
                onDismiss = { showTrash = false },
                onRestore = {
                    viewModel.restoreBook(it)

                    onShowSnackbar("Libro restaurado con éxito", NotificationType.Online)

                    showTrash = false

                },
                onDeleteForever = {
                    viewModel.deleteBookForever(it)

                    onShowSnackbar("Libro eliminado permanentemente", NotificationType.Deleted)

                }
            )
        }
    }
}

