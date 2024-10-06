package com.uuranus.compose.effects.x

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Loading(
    modifier: Modifier = Modifier,
) {

    val numOfLoadingBar = 8
    val radianDist = 2 * PI / numOfLoadingBar

    val infiniteRepeatable = rememberInfiniteTransition("loading")

    val rotation by infiniteRepeatable.animateValue(
        initialValue = 0,
        targetValue = numOfLoadingBar,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = LinearEasing),
        ),

        label = "loading"
    )

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        var currentRadian = 0.0

        val centerX = size.width / 2
        val centerY = size.height / 2

        val centerRadius = (size.minDimension) / 6f
        val radius = (size.minDimension / 2) - centerRadius
        val barWidth = centerRadius / 2

        repeat(numOfLoadingBar) { index ->

            val topLeftX = centerX + radius * sin(currentRadian).toFloat()
            val topLeftY = centerY - radius * cos(currentRadian).toFloat()

            val bottomLeftX = centerX + centerRadius * sin(currentRadian).toFloat()
            val bottomLeftY = centerY - centerRadius * cos(currentRadian).toFloat()

            val color = if (rotation == index) {
                Color.Gray
            } else if ((rotation + numOfLoadingBar - 1) % numOfLoadingBar == index) {
                Color.Gray.copy(alpha = 0.8f)
            } else if ((rotation + numOfLoadingBar - 2) % numOfLoadingBar == index) {
                Color.Gray.copy(alpha = 0.6f)
            } else if ((rotation + numOfLoadingBar - 3) % numOfLoadingBar == index) {
                Color.Gray.copy(alpha = 0.4f)
            } else {
                Color.Gray.copy(alpha = 0.2f)
            }

            drawLine(
                color = color,
                start = Offset(
                    topLeftX,
                    topLeftY
                ),
                end = Offset(
                    bottomLeftX,
                    bottomLeftY
                ),
                strokeWidth = barWidth,
                cap = StrokeCap.Round
            )

            currentRadian += radianDist
        }

    }
}