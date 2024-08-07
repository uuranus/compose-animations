package com.uuranus.compose.effects

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
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

