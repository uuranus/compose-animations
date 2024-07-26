package com.uuranus.compose.effects

import android.annotation.SuppressLint
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
import androidx.compose.ui.geometry.Offset
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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

//@Composable
//fun ShimmerEffect(
//    modifier: Modifier = Modifier,
//) {
//
//    val transition = rememberInfiniteTransition(label = "")
//    val translateAnim = transition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(
//                durationMillis = 1000,
//                easing = LinearEasing
//            )
//        ), label = ""
//    )
//
//    val transformationMatrix = Matrix()
//
//    Canvas(
//        modifier = modifier
//    ) {
//        val paint = Paint()
//
//        val gradientFrom = Offset(-size.width / 2, 0f)
//        val gradientTo = -gradientFrom
//
//        val progress = translateAnim.value
//
//        val traversal = -size.width / 2 + size.width * progress - size.width / 2
//
//        transformationMatrix.apply {
//            reset()
//            translate(
//                translateAnim.value * size.width, translateAnim.value * size.height
//            )
//            rotateZ(45f)
//            translate(-translateAnim.value * size.width, -translateAnim.value * size.height, 0f)
//            translate(traversal, 0f, 0f)
//        }
//
//        val gradientWidth = size.width / 5
//
//        paint.shader = LinearGradientShader(
//            from = Offset(-gradientWidth / 2 + translateAnim.value * size.width, 0f),
//            to = Offset(gradientWidth / 2 + translateAnim.value * size.width, 0f),
//            colors = listOf(
//                Color.Transparent,
//                Color.White,
//                Color.Transparent
//            ),
//        )
//
//        val drawArea = size.toRect()
//        drawIntoCanvas { canvas ->
//            canvas.withSaveLayer(
//                bounds = drawArea,
//                paint = Paint(),
//            ) {
//                canvas.drawRect(drawArea, paint)
//            }
//        }
//    }
//}

//@Composable
//fun ShimmeringEffect(
//    isLoading: Boolean,
//    contentAfterLoading: @Composable () -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    if (isLoading) {
//        var size by remember {
//            mutableStateOf(IntSize.Zero)
//        }
//
//        Row(
//            modifier = modifier
//                .onGloballyPositioned {
//                    size = it.size
//                }
//        ) {
//
//            Box(
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(
//                        shape = RoundedCornerShape((size.height / 2).toDp())
//                    )
//                    .background(
//                        Color(0xFF1C1A1D),
//                    )
//                    .shimmerEffect()
//            )
//            Spacer(modifier = Modifier.width(16.dp))
//            Column(modifier = Modifier.weight(1f)) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(20.dp)
//                        .clip(
//                            shape = RoundedCornerShape((size.height / 2).toDp())
//                        )
//                        .background(
//                            Color(0xFF1C1A1D)
//                        )
//                        .shimmerEffect()
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth(0.7f)
//                        .height(20.dp)
//                        .clip(
//                            shape = RoundedCornerShape((size.height / 2).toDp())
//                        )
//                        .background(
//                            Color(0xFF1C1A1D),
//                        )
//                        .shimmerEffect()
//                )
//            }
//        }
//    } else {
//        contentAfterLoading()
//    }
//}

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

        val transition = rememberInfiniteTransition(label = "")
        val startOffsetX by transition.animateFloat(
            initialValue = -shadowWidth,
            targetValue = shadowWidth,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 3000,
                    easing = LinearEasing
                )
            ),
            label = ""
        )

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
                    .graphicsLayer {
                        translationX = startOffsetX
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(shadowWidthDp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xff5e5c5f).copy(alpha = 0.6f),
                                    Color(0xff5e5c5f).copy(alpha = 0.9f),
                                    Color(0xff5e5c5f).copy(alpha = 0.6f),
                                ),
                            ),
                        )
                ) {}
            }

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
                        .graphicsLayer {
                            translationX = startOffsetX
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(shadowWidthDp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xff5e5c5f).copy(alpha = 0.6f),
                                        Color(0xff5e5c5f).copy(alpha = 0.9f),
                                        Color(0xff5e5c5f).copy(alpha = 0.6f),
                                    ),
                                ),
                            )
                    ) {}
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp)
                        .clip(
                            shape = RoundedCornerShape((size.height / 2).toDp())
                        )
                        .background(
                            Color(0xFF1C1A1D),
                        )
                        .graphicsLayer {
                            translationX = startOffsetX
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(shadowWidthDp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xff5e5c5f).copy(alpha = 0.6f),
                                        Color(0xff5e5c5f).copy(alpha = 0.9f),
                                        Color(0xff5e5c5f).copy(alpha = 0.6f),
                                    ),
                                ),
                            )
                    ) {}
                }
            }
        }
    } else {
        contentAfterLoading()
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val shadowWidth = with(LocalDensity.current) {
        100.dp.toPx()
    }

    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                easing = LinearEasing
            )
        ),
        label = ""
    )

    background(
        brush = Brush.horizontalGradient(
            colors = listOf(
                Color.Transparent,
                Color(0xff5e5c5f).copy(alpha = 1.0f),
                Color(0xff5e5c5f).copy(alpha = 1.0f),
                Color(0xff5e5c5f).copy(alpha = 1.0f),
                Color.Transparent
            ),
            startX = startOffsetX,
            endX = startOffsetX + shadowWidth,
            tileMode = TileMode.Decal
        ),
    )
        .onGloballyPositioned {
            size = it.size
        }

}

