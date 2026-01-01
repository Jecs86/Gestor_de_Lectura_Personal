package com.appstudio.gestordelecturapersonal.ui.screen.notes

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.appstudio.gestordelecturapersonal.data.local.db.DatabaseProvider
import com.appstudio.gestordelecturapersonal.ui.component.AppTopBar
import com.appstudio.gestordelecturapersonal.ui.navigation.AppRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navController: NavController,
    bookId: Long,
    onBackPage: () -> Unit
) {

    val context = LocalContext.current

    val viewModel: NotesViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = DatabaseProvider.getDatabase(context)
                return NotesViewModel(
                    noteDao = database.noteDao(),
                    bookId = bookId
                ) as T
            }
        }
    )

    val notes by viewModel.notes.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Notas",
                needBackPage = true,
                onBackPage = onBackPage
            )
        }
    ) { padding ->
        NotesContent(
            modifier = Modifier.padding(padding),
            notes = notes,
            navController = navController,
            bookId = bookId,
            onEditNote = { noteId ->
                navController.navigate(
                    AppRoutes.EditNote.createRoute(bookId, noteId)
                )
            }
        )
    }
}