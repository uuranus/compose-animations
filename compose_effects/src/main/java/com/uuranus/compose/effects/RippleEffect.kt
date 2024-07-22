package com.uuranus.compose.effects

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun RippleEffect() {
    val rippleRadius = remember { Animatable(0f) }

    // Infinite loop for animating the ripple
    LaunchedEffect(Unit) {
        while (true) {
            rippleRadius.animateTo(
                targetValue = 2000f, // You can adjust this value based on your needs
                animationSpec = tween(durationMillis = 2000, easing = { it })
            )
            rippleRadius.snapTo(0f)
            delay(500) // Delay before the next ripple effect starts
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRippleEffect(rippleRadius.value, color = Color.Blue.copy(alpha = 0.5f))
    }
}

fun DrawScope.drawRippleEffect(radius: Float, color: Color) {
    drawCircle(
        color = color,
        radius = radius,
        center = center
    )
}

@Composable
fun WaveEffect() {
    val duration = 500L
    val delayPerItem = 100L
    val transition = rememberInfiniteTransition(label = "")

    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        "Hello".forEachIndexed { index, c ->
            val scale by transition.animateFloat(
                initialValue = 1f,
                targetValue = 2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = duration.toInt(),
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(offsetMillis = ((delayPerItem * index) % duration).toInt())
                ),
                label = "",
            )
            Text(text = c.toString(), fontSize = (24 * scale).sp)
        }
    }
}

fun DrawScope.drawWave(offsetX: Float, size: Size, color: Color) {

}