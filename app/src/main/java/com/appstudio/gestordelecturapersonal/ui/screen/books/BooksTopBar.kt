package com.appstudio.gestordelecturapersonal.ui.screen.books

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Mis Libros",
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}
