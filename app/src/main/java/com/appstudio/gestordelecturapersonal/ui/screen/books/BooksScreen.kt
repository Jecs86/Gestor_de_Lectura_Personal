package com.appstudio.gestordelecturapersonal.ui.screen.books

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.appstudio.gestordelecturapersonal.ui.component.AppBottomBar


@Composable
fun BooksScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            BooksTopBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: agregar libro */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar libro"
                )
            }
        },
        bottomBar = {
            AppBottomBar(navController)
        }
    ) { padding ->
        BooksContent(
            modifier = Modifier.padding(padding),
            libros = fakeBooks
        )
    }
}

