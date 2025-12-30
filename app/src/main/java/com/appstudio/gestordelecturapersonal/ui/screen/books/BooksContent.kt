package com.appstudio.gestordelecturapersonal.ui.screen.books


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BooksContent(
    modifier: Modifier = Modifier,
    libros: List<BookUiModel>
) {
    LazyVerticalGrid(
        modifier = modifier.padding(16.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(libros) { libro ->
            BookCard(book = libro)
        }
    }
}
