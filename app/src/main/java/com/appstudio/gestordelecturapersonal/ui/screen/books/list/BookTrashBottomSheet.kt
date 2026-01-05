package com.appstudio.gestordelecturapersonal.ui.screen.books.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.appstudio.gestordelecturapersonal.ui.component.DeleteDialog
import com.appstudio.gestordelecturapersonal.ui.component.NotificationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksTrashBottomSheet(
    books: List<DeletedBookUiModel>,
    onDismiss: () -> Unit,
    onRestore: (Long) -> Unit,
    onDeleteForever: (Long) -> Unit
) {

    var selectedBook by remember { mutableStateOf<DeletedBookUiModel?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .padding(16.dp)
        ) {
            Text(
                text = "Papelera",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (books.isEmpty()) {
                Text("La papelera está vacía")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(books) { book ->
                        DeletedBookItem(
                            book = book,
                            onRestore = { onRestore(book.id) },
                            onDeleteForever = {
                                selectedBook = book
                                showDeleteDialog = true
                            }
                        )
                    }
                }

                if (showDeleteDialog && selectedBook != null) {
                    DeleteDialog(
                        element = selectedBook!!.titulo,
                        elementTitle = "Libro",
                        onConfirm = {
                            onDeleteForever(selectedBook!!.id)
                            selectedBook = null
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
        }
    }
}