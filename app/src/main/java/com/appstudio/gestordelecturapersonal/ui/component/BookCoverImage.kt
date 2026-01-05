package com.appstudio.gestordelecturapersonal.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent

@Composable
fun BookCoverImage(
    url: String?,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = "Portada del libro",
        modifier = modifier.clip(RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Crop,
    ) {
        val state = painter.state
        if (state is AsyncImagePainter.State.Loading) {
            // Efecto Shimmer (Skeleton)
            val transition = rememberInfiniteTransition(label = "shimmer")
            val translateAnim = transition.animateFloat(
                initialValue = 0f,
                targetValue = 1000f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ), label = "shimmer"
            )

            val brush = Brush.linearGradient(
                colors = listOf(
                    Color.LightGray.copy(alpha = 0.6f),
                    Color.LightGray.copy(alpha = 0.2f),
                    Color.LightGray.copy(alpha = 0.6f),
                ),
                start = Offset.Zero,
                end = Offset(x = translateAnim.value, y = translateAnim.value)
            )
            Box(modifier = Modifier.fillMaxSize().background(brush))
        } else if (state is AsyncImagePainter.State.Error || url.isNullOrBlank()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
                Text(
                    text = "Sin imagen",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        } else {
            SubcomposeAsyncImageContent()
        }
    }
}