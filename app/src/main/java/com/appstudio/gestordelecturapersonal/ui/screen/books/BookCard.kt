package com.appstudio.gestordelecturapersonal.ui.screen.books

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BookCard(
    book: BookUiModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            // Portada (placeholder)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = book.titulo,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = book.autor,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { book.progreso / 100f },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${book.paginasLeidas}/${book.paginasTotales} páginas",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
