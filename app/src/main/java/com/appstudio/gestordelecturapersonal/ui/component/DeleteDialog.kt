package com.appstudio.gestordelecturapersonal.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteDialog(
    element: String,
    elementTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isPermanent: Boolean = false
) {

    val shortElement = if (element.length > 15) element.take(12) + "..." else element

    val message: String = if (isPermanent) "Esta acción eliminará \"$shortElement\" definitivamente."
                            else "Esta acción enviará \"$shortElement\" a la papelera."

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Eliminar $elementTitle")
        },
        text = {
            Text(
                text = "¿Estás seguro de que deseas eliminar \"$shortElement\"?\n\n$message"
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}