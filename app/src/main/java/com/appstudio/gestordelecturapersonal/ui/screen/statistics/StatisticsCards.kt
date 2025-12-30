package com.appstudio.gestordelecturapersonal.ui.screen.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatisticsCards(
    uiState: StatisticsUiState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        StatCard(
            title = "Libros",
            value = uiState.totalLibros.toString()
        )

        StatCard(
            title = "Leídos",
            value = uiState.librosLeidos.toString()
        )

        StatCard(
            title = "En progreso",
            value = uiState.librosEnProgreso.toString()
        )
    }
}

@Composable
private fun RowScope.StatCard(
    title: String,
    value: String
) {
    Card(
        modifier = Modifier.weight(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
