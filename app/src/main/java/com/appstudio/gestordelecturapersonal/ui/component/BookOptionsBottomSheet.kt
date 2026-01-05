package com.appstudio.gestordelecturapersonal.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.appstudio.gestordelecturapersonal.R
import com.appstudio.gestordelecturapersonal.ui.screen.books.list.BookUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookOptionsBottomSheet(
    book: BookUiModel,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onViewNotes: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        windowInsets = WindowInsets(0)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            BookCoverImage(
                url = book.urlPortada,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            Text(
                text = book.titulo,
                style = MaterialTheme.typography.titleLarge
            )

            Divider()

            Button(onClick = onEdit, modifier = Modifier.fillMaxWidth()) {
                Text("✏️ Editar libro")
            }

            Button(onClick = onViewNotes, modifier = Modifier.fillMaxWidth()) {
                Text("📖 Ver notas")
            }

            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("🗑️ Eliminar libro")
            }

            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar")
            }
        }
    }
}