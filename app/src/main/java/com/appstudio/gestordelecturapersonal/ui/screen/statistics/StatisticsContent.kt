package com.appstudio.gestordelecturapersonal.ui.screen.statistics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun StatisticsContent(
    paddingValues: PaddingValues,
    viewModel: StatisticsViewModel
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        StatItem("Libros totales", state.totalBooks)
        StatItem("Libros leídos", state.readBooks)
        StatItem("Libros pendientes", state.pendingBooks)
        StatItem("Páginas leídas", state.pagesRead)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Progreso general: ${state.progressPercent.toInt()}%",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        BooksBarChart(
            read = state.readBooks,
            pending = state.pendingBooks,
            total = state.totalBooks
        )
    }
}
