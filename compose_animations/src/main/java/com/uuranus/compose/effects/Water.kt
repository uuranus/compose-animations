package com.uuranus.compose.effects

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun Water(
    waterLevel: Float,
) = BoxWithConstraints {
    require(waterLevel in 0f..1f) { " 여기 사이에 있었야 함" }

    val density = LocalDensity.current
    val height = with(density) {
        maxHeight.toPx()
    }
    val width = with(density) {
        maxWidth.toPx()
    }

    val waterHeightList = calculateYs2(height = height.toInt(), waterLevel = waterLevel)
    val waterHeightList2 = calculateYs2(height = height.toInt(), waterLevel = waterLevel)
    val waterHeightList3 = calculateYs2(height = height.toInt(), waterLevel = waterLevel)

    val currentY = height * waterLevel

    Canvas(modifier = Modifier.fillMaxSize()) {


        val wavePath = DrawWave(
            yList = waterHeightList,
            width = width.toInt(),
            height = height.toInt(),
            waterHeight = currentY
        )


        val wavePath2 = DrawWave(
            yList = waterHeightList2,
            width = width.toInt(),
            height = height.toInt(),
            waterHeight = currentY
        )

        val wavePath3 = DrawWave(
            yList = waterHeightList3,
            width = width.toInt(),
            height = height.toInt(),
            waterHeight = currentY
        )


        drawPath(
            path = wavePath,
            color = Color.Blue,
        )

        drawPath(
            path = wavePath2,
            color = Color.Gray,
        )


        drawPath(
            path = wavePath3,
            color = Color.Red,
        )


    }
}

fun DrawWave(yList: List<Int>, width: Int, height: Int, waterHeight: Float): Path =
    Path().apply {

        moveTo(0f, waterHeight)

        val interval = width * (1 / (yList.size + 1).toFloat())

        yList.forEachIndexed { idx, y ->
            val segmentIndex = (idx + 1) / (yList.size + 1).toFloat()

            val x = width * segmentIndex

            cubicTo(
                x1 = if (idx == 0) 0f else x - interval / 2f,
                y1 = yList.getOrNull(idx - 1)?.toFloat() ?: waterHeight,

                x2 = x - interval / 2f,
                y2 = y.toFloat(),

                x3 = x,
                y3 = y.toFloat()
            )

        }

        cubicTo(
            x1 = width - interval / 2f,
            y1 = yList.last().toFloat(),
            x2 = width.toFloat(),
            y2 = waterHeight,
            x3 = width.toFloat(),
            y3 = waterHeight
        )

        lineTo(width.toFloat(), height.toFloat())
        lineTo(0f, height.toFloat())

        close()
    }

@Composable
fun calculateY(
    height: Int,
    waterLevvel: Float,
): Int {
    var y by remember {
        mutableIntStateOf(0)
    }

    val duration = Random.nextInt(300) + 300

    val yNoiseAnimation = rememberInfiniteTransition(label = "")

    val yNoise by yNoiseAnimation.animateFloat(
        initialValue = 25f, targetValue = -25f, animationSpec =
        infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    y = ((waterLevvel * height).toInt() + yNoise).toInt()

    val springY by animateIntAsState(
        targetValue = y,
        animationSpec = spring(
            dampingRatio = 0.5f,
            stiffness = 100f,
        ), label = ""
    )

    return springY
}

@Composable
fun calculateYs2(
    height: Int,
    waterLevel: Float,
): List<Int> {
    val total = 6
    return (0..total).map {
        calculateY(height = height, waterLevvel = waterLevel)
    }.toList()
}