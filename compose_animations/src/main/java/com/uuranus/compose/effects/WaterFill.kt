package com.uuranus.compose.effects

import android.content.res.Resources
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.view.ContentInfoCompat.Flags
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun WaterFill() {

    var contentHeight by remember {
        mutableStateOf(0.dp)
    }

    Column(modifier = Modifier.onGloballyPositioned {
        contentHeight = it.size.height.toDp()
    }, verticalArrangement = Arrangement.Bottom) {

        var targetHeight by remember {
            mutableStateOf(100.dp)
        }
        val height by animateDpAsState(
            targetValue = targetHeight,
            animationSpec = spring(
                dampingRatio = 0.5f,
                stiffness = Spring.StiffnessLow
            ), label = "waterHeight"
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(Color(0xff10BBE5))
                .clickable {
                    targetHeight = contentHeight.coerceAtMost(targetHeight + 200.dp)

                    if (targetHeight == contentHeight) {
                        targetHeight = 100.dp
                    }
                }
        )
    }
}

@Composable
fun WaterWave(
    modifier: Modifier = Modifier,
) {

    val xAxisAnimation = rememberInfiniteTransition(label = "")

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val duration = 3000

    val xAxis by xAxisAnimation.animateFloat(
        initialValue = 0f, targetValue = size.width.toFloat(), animationSpec =
        infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    val interval by remember {
        mutableFloatStateOf(size.width.toFloat() / 10f)
    }

    val xAxises by remember {
        mutableStateOf(calculateXs(10, size.width.toFloat()))
    }

    val path = Path().apply {
        xAxises.forEachIndexed { idx, x ->
            val x1 = if (idx == 0) 0f else x - interval / 2f

            val x2 = x - interval / 2f



            cubicTo(
                x1 = x1,
                y1 = sin(x1),

                x2 = x2,
                y2 = sin(x2),

                x3 = x,
                y3 = sin(x)
            )

        }
    }


    Canvas(modifier = Modifier
        .fillMaxSize()
        .onGloballyPositioned {
            size = it.size
        }) {


        drawPath(
            path = path,
            color = Color(0xff10BBE5)
        )

    }
}

fun calculateXs(
    interval: Int,
    waterWidth: Float,
): List<Float> {
    return (0..interval).map {
        it * waterWidth / interval
    }.toList()
}


fun Int.toDp(): Dp = (this / Resources.getSystem().displayMetrics.density).dp