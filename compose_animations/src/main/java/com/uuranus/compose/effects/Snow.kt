package com.uuranus.compose.effects

import androidx.annotation.Dimension.Companion.DP
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


@Composable
fun SnowScreen() {

    val density = LocalDensity.current

    val screenWidth =
        (Dp(LocalConfiguration.current.screenWidthDp.toFloat())).dpToPx(density)
    val screenHeight = (Dp(LocalConfiguration.current.screenHeightDp.toFloat())).dpToPx(density)


    var snowState by remember {
        mutableStateOf(SnowState(createSnowList(IntSize(screenWidth, screenHeight))))
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            awaitFrame()
            for (snow in snowState.snows) {
                snow.update()
            }
            snowState = snowState.copy(snows = snowState.snows)
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.Black
            )
    ) {
        val canvas = drawContext.canvas
        for (snow in snowState.snows) {
            snow.draw(canvas)
        }
    }
}

data class SnowState(
    val snows: List<Snow>,
)


private const val angleSeed = 25.0f
private val angleSeedRange = -angleSeed..angleSeed
private val incrementRange = 0.4f..0.8f

class Snow(
    val size: Float,
    position: Offset,
    private val screenSize: IntSize,
    incrementRange: ClosedFloatingPointRange<Float>,
    angle: Float,
) {
    val paint = Paint().apply {
        isAntiAlias = true
        color = Color.White
        style = PaintingStyle.Fill
    }


    private var position by mutableStateOf(position)

    private var angle by mutableFloatStateOf(angle)

    fun draw(canvas: Canvas) {
        canvas.drawCircle(position, size, paint)
    }

    fun update() {
        val increment = incrementRange.random()

        angle = angleSeedRange.random() * (PI / 180).toFloat()
        val xAngle = (increment * sin(angle))
        val yAngle = (increment * cos(angle))

        angle += angleSeedRange.random() / 1000f

        position =
            if (position.y > screenSize.height) {
                position.copy(y = 0f)
            } else {
                position.copy(
                    x = position.x + xAngle,
                    y = position.y + yAngle
                )
            }
    }
}

fun createSnowList(canvas: IntSize): List<Snow> {
    return List(30) {
        Snow(
            size = 20f,
            position = Offset(
                x = canvas.width.randomTest().toFloat(),
                y = canvas.height.randomTest().toFloat()
            ),
            canvas,
            incrementRange = incrementRange.randomRange(),
            angle = (angleSeedRange.random() / angleSeed * 0.1f + (Math.PI.toFloat()) / 2.0f)
        )
    }
}

fun Int.randomTest() = Random.nextInt(this)

fun Dp.dpToPx(density: Density): Int {
    return (this.value * density.density).toInt()
}

fun ClosedFloatingPointRange<Float>.random(): Float {
    return (Random.nextFloat() * (endInclusive - start) + start)
}

fun ClosedFloatingPointRange<Float>.randomRange(): ClosedFloatingPointRange<Float> {
    val start = this.random()
    val end = this.random().coerceAtLeast(start)
    return start..end
}