package com.uuranus.compose.effects.instagram

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.uuranus.variousshapes.RingShape
import kotlinx.coroutines.launch

internal val instagramColorList = listOf(
    Color(0xFFFFD600),
    Color(0xFFFF7A00),
    Color(0xFFFF0069),
    Color(0xFFD300C5),
    Color(0xFF7638FA)
)

@Composable
fun InstagramProgress(
    modifier: Modifier = Modifier,
    isComplete: Boolean,
    colorList: List<Color> = instagramColorList,
    rotateDuration: Int = 500,
) {

    val scope = rememberCoroutineScope()

    val rotationAnimation = remember { Animatable(0f) }
    val rotation by remember { derivedStateOf { rotationAnimation.value % 360f } }

    LaunchedEffect(isComplete) {
        if (isComplete) {
            while (rotationAnimation.isRunning) {
                val remainAngle = rotationAnimation.value + (360f - rotation)
                rotationAnimation.animateTo(
                    targetValue = remainAngle,
                    animationSpec = tween(
                        durationMillis = (360f - rotation).toInt()
                                * (rotateDuration / 360f).toInt(),
                        easing = LinearEasing
                    )
                )
            }
        } else {
            scope.launch {
                while (true) {
                    rotationAnimation.animateTo(
                        targetValue = rotationAnimation.value + 360f,
                        animationSpec = tween(
                            durationMillis = rotateDuration,
                            easing = LinearEasing
                        )
                    )
                }
            }
        }
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                rotationZ = rotation
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .align(Alignment.Center)
                .background(
                    brush = Brush.linearGradient(colorList),
                    shape = RingShape(
                        ringWidth = 12.dp
                    )
                )
        )

        if (isComplete && rotationAnimation.isRunning.not()) {
            CheckMarkAnimation(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                colorList = colorList
            )
        }

    }

}


@Composable
private fun CheckMarkAnimation(modifier: Modifier, colorList: List<Color>) {

    val infiniteTransition = rememberInfiniteTransition(label = "pathLength")
    val pathLength by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2700
                0f at 0 with LinearEasing
                0.3f at 300 with LinearEasing
                0.3f at 400
                1f at 700 with LinearEasing
                1f at 2600
            },
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(2000)
        ), label = "pathLength"
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCheckMark(size, pathLength, colorList = colorList)
            }
        }
    }
}

private fun DrawScope.drawCheckMark(
    size: Size,
    progress: Float,
    colorList: List<Color>,
) {
    val height = (size.width * 2 / 3).toInt()
    val width = size.width

    val centerY = size.height / 2

    val curvePoint = width / 3
    val startY = centerY - height / 2
    val endY = centerY + height / 2

    val path = Path().apply {
        moveTo(0f, centerY)
        lineTo(curvePoint, endY)
        lineTo(size.width, startY)
    }

    val pathMeasure = PathMeasure().apply {
        setPath(path, forceClosed = false)
    }

    val pathSegment = Path()

    pathMeasure.getSegment(
        startDistance = 0f,
        stopDistance = pathMeasure.length * progress,
        pathSegment,
        startWithMoveTo = true
    )

    drawPath(
        path = pathSegment,
        color = colorList[colorList.size / 2],
        style = Stroke(
            width = 12.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )

}
