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
import com.patrykandpatrick.vico.compose.component.shape.roundedCornerShape
import com.patrykandpatrick.vico.compose.legend.verticalLegend
import com.patrykandpatrick.vico.compose.legend.legendItem
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.dimensions.MutableDimensions
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.core.legend.HorizontalLegend

@Composable
fun BooksBarChart(
    read: Int,
    pending: Int,
    total: Int,
) {
    if (total == 0) return

    val charEntryModel = entryModelOf(
        listOf(entryOf(0,read)),
        listOf(entryOf(1, pending))
    )

    val max_view = if (read >= pending) read else pending

    val colorRead = MaterialTheme.colorScheme.primary.toArgb()
    val colorPending = MaterialTheme.colorScheme.tertiary.toArgb()
    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()

    val legend = HorizontalLegend(
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
        iconSizeDp = 10.dp.value,
        iconPaddingDp = 2.dp.value,
        spacingDp = 24.dp.value,
        padding = MutableDimensions(16f, 0f)
    )

    Chart(
        chart = columnChart(
            columns = listOf(
                LineComponent(
                    color = colorRead,
                    thicknessDp = 16f,
                    shape = Shapes.roundedCornerShape(4.dp)
                ),
                LineComponent(
                    color = colorPending,
                    thicknessDp = 16f,
                    shape = Shapes.roundedCornerShape(4.dp)
                )
            )
        ),
        model = charEntryModel,
        startAxis = rememberStartAxis(
            valueFormatter = { value, _ -> if (value % 1f == 0f) value.toInt().toString() else "" },
            itemPlacer = AxisItemPlacer.Vertical.default(
                maxItemCount = if (max_view <= 5) max_view + 1 else 6
            )
        ),
        bottomAxis = rememberBottomAxis(
            label = null,
            tick = null,
            guideline = null
        ),
        legend = legend,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}