package com.uuranus.compose.effects

import android.annotation.SuppressLint
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.composed
import androidx.compose.material3.Text
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex

@Composable
fun ShimmeringBox(content: @Composable BoxScope.() -> Unit) {

    var contentSize by remember {
        mutableIntStateOf(0)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .onGloballyPositioned { coordinates ->
                contentSize = coordinates.size.width
            },
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(12.dp))
                .drawBehind {
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Magenta,
                                Color.Transparent
                            ),
                            start = Offset.Zero,
                            end = Offset(x = contentSize * 1.5f, y = 0f),
                            tileMode = TileMode.Clamp
                        )
                    )
                }
                .zIndex(1f)
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .zIndex(0f),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }

}

@Composable
fun ShimmeringEffect(
    isLoading: Boolean,
    contentAfterLoading: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isLoading) {
        var size by remember {
            mutableStateOf(IntSize.Zero)
        }

        val shadowWidth = size.width / 4f

        val shadowWidthDp = with(LocalDensity.current) {
            shadowWidth.toDp()
        }

        Row(
            modifier = modifier
                .onGloballyPositioned {
                    size = it.size
                }
        ) {

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(
                        shape = RoundedCornerShape((size.height / 2).toDp())
                    )
                    .background(
                        Color(0xFF1C1A1D),
                    )
                    .shimmerEffect(
                        size = size,
                        shadowWidthDp = shadowWidthDp
                    )
            )

            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .clip(
                            shape = RoundedCornerShape((size.height / 2).toDp())
                        )
                        .background(
                            Color(0xFF1C1A1D)
                        )
                        .shimmerEffect(
                            size = size,
                            shadowWidthDp = shadowWidthDp
                        )
                )

                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .clip(
                            shape = RoundedCornerShape((20.dp / 2)) // Adjust accordingly
                        )
                        .background(
                            Color(0xFF1C1A1D),
                        )
                        .shimmerEffect(
                            size = size,
                            shadowWidthDp = shadowWidthDp
                        )
                ) {
                    // Your content here
                }
            }
        }
    } else {
        contentAfterLoading()
    }
}

fun Modifier.shimmerEffect(size: IntSize, shadowWidthDp: Dp): Modifier = composed {

    val transition = rememberInfiniteTransition(label = "")
    val shadowWidthPx = with(LocalDensity.current) {
        shadowWidthDp.toPx()
    }

    val startOffsetX by transition.animateFloat(
        initialValue = -shadowWidthPx,
        targetValue = size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = EaseInOut
            ),
        ),
        label = ""
    )

    val brush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xff5e5c5f).copy(alpha = 0.6f),
            Color(0xff5e5c5f).copy(alpha = 0.9f),
            Color(0xff5e5c5f).copy(alpha = 0.6f),
        ),
        startX = 0f,
        endX = shadowWidthPx
    )

    graphicsLayer {
        translationX = startOffsetX
    }.drawWithContent {
        val shimmerWidthPx = shadowWidthDp.toPx()

        drawRect(
            brush = brush,
            topLeft = Offset(x = 0f, y = 0f),
            size = Size(shimmerWidthPx, size.height.toFloat())
        )
    }
}


