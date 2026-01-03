package com.appstudio.gestordelecturapersonal.ui.screen.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.legend.verticalLegend
import com.patrykandpatrick.vico.compose.legend.legendItem
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.dimensions.MutableDimensions

@Composable
fun BooksBarChart(
    read: Int,
    pending: Int,
    total: Int
) {
    if (total == 0) return

    val charEntryModel = entryModelOf(read, pending)

    val colorRead = MaterialTheme.colorScheme.primary.toArgb()
    val colorPending = MaterialTheme.colorScheme.secondary.toArgb()
    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()

    val legend = verticalLegend(
        items = listOf(
            legendItem(
                icon = ShapeComponent(shape = Shapes.pillShape, color = colorRead),
                label = TextComponent.Builder().apply { this.color = textColor }.build(),
                labelText = "Leídos"
            ),
            legendItem(
                icon = ShapeComponent(shape = Shapes.pillShape, color = colorPending),
                label = TextComponent.Builder().apply { this.color = textColor }.build(),
                labelText = "Pendientes"
            )
        ),
        iconSize = 10.dp,
        iconPadding = 8.dp,
        spacing = 4.dp,
        padding = MutableDimensions(8f, 8f)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Chart(
            chart = columnChart(),
            model = charEntryModel,
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(),
            legend = legend,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}