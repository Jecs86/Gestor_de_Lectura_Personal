package com.appstudio.gestordelecturapersonal.ui.screen.statistics

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun StatItem(
    label: String,
    value: Int
) {
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.bodyLarge
    )
}