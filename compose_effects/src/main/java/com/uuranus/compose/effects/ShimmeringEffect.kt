package com.uuranus.compose.effects

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

@Composable
fun ShimmeringPlaceholder(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
) {

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val shimmeringWidth = size.width / 4f

    val placeholderColor = if (backgroundColor.luminance() > 0.5f) {
        Color(0xFFD3D3D3).copy(alpha = 0.6f)
    } else {
        Color(0xFF4F4F4F).copy(alpha = 0.6f)
    }

    Box(
        modifier = modifier
            .onGloballyPositioned {
                size = it.size
            }
            .clip(
                shape = RoundedCornerShape((size.height / 2).toDp())
            )
            .background(placeholderColor)
            .shimmerEffect(
                size = size,
                shimmeringWidthPx = shimmeringWidth,
                placeholderColor = placeholderColor
            )
    )
}

fun Modifier.shimmerEffect(
    size: IntSize,
    shimmeringWidthPx: Float,
    placeholderColor: Color,
): Modifier =
    composed {

        val transition = rememberInfiniteTransition(label = "")

        val startOffsetX by transition.animateFloat(
            initialValue = -shimmeringWidthPx,
            targetValue = size.width.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1500,
                    easing = FastOutSlowInEasing
                ),
            ),
            label = ""
        )

        val shimmerColor = if (placeholderColor.luminance() > 0.5f) {
            Color.White.copy(alpha = 0.4f)
        } else {
            Color.Gray.copy(alpha = 0.4f)
        }

        val brush = Brush.horizontalGradient(
            colors = listOf(
                shimmerColor.copy(alpha = 0.1f),
                shimmerColor.copy(alpha = 0.4f),
                shimmerColor.copy(alpha = 0.1f)
            ),
            startX = 0f,
            endX = shimmeringWidthPx
        )

        graphicsLayer {
            translationX = startOffsetX
        }.drawWithContent {
            drawRect(
                brush = brush,
                topLeft = Offset(x = 0f, y = 0f),
                size = Size(shimmeringWidthPx, size.height.toFloat())
            )
        }
    }


