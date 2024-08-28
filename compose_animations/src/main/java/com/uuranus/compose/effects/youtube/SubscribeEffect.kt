package com.uuranus.compose.effects.youtube

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PendulumEffectAnimation(
    modifier: Modifier = Modifier,
    initialAngle: Float = 20f,
    dampingFactor: Float = 0.6f,
    isHanging: Boolean,
    startFromInitialAngle: Boolean,
) {

    var rotation by remember {
        mutableFloatStateOf(if (startFromInitialAngle) initialAngle else 0f)
    }

    var angle by remember { mutableFloatStateOf(initialAngle) }

    var size by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(isHanging) {
        if (isHanging) {
            launch {
                while (angle >= 1f) {
                    if (startFromInitialAngle && angle == initialAngle) {
                        animate(
                            initialValue = angle,
                            targetValue = 0f,
                            animationSpec = keyframes {
                                durationMillis = 300
                                angle at 75 with LinearEasing
                                0f at 150 with LinearEasing
                                -angle at 225 with LinearEasing
                                0f at 300 with LinearEasing
                            },
                        ) { value, _ ->
                            rotation = value
                        }
                    } else {
                        animate(
                            initialValue = 0f,
                            targetValue = 0f,
                            animationSpec = keyframes {
                                durationMillis = 300
                                0f at 0 with LinearEasing
                                angle at 75 with LinearEasing
                                0f at 150 with LinearEasing
                                -angle at 225 with LinearEasing
                                0f at 300 with LinearEasing
                            },
                        ) { value, _ ->
                            rotation = value
                        }

                    }

                    angle *= dampingFactor
                }
                angle = initialAngle
            }
        } else {
            angle = initialAngle
        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                size = it.size
            },
        contentAlignment = Alignment.Center
    ) {
        val iconSize = with(LocalDensity.current) {
            minOf(size.width, size.height).toDp()
        }
        val radius = with(LocalDensity.current) {
            iconSize.toPx() / 2
        }

        Box(
            modifier = Modifier
                .size(iconSize)
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize)
                    .graphicsLayer {

                        val centerY = size.height / 2
                        val angleRadians = Math.toRadians(rotation.toDouble())

                        this.translationX = -radius * sin(angleRadians).toFloat()
                        this.translationY = centerY - radius * cos(angleRadians).toFloat()

                        this.rotationZ = rotation
                    }
            )
        }
    }
}