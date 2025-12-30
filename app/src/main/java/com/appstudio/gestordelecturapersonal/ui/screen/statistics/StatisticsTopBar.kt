package com.appstudio.gestordelecturapersonal.ui.screen.statistics

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Estadísticas",
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}
