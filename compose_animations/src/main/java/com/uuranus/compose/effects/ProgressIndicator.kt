package com.uuranus.compose.effects

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DotLoadingIndicator(
    modifier: Modifier = Modifier,
) {

    val infiniteAnimation = rememberInfiniteTransition("scale")

    val scaleValues = List(3) { index ->
        infiniteAnimation.animateFloat(
            initialValue = 1f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1500
                    1f at index * 300 using LinearEasing
                    1.3f at index * 300 + 300 using LinearEasing
                    1f at index * 300 + 600 using LinearEasing
                },
                repeatMode = RepeatMode.Restart
            ),
            label = "scale"
        )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .scale(scaleValues[it].value)
                    .background(
                        color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        shape = CircleShape
                    )
            )
        }
    }

}