package com.uuranus.compose.effects.instagram.dotindicator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.uuranus.compose.effects.Picture

class DotIndicator(
    private val spacePadding: Dp,
    private val totalPage: Int,
    private val currentPage: Int,
    private val left: Int,
    private val fullDotLeft: Int,
    private val fullDotRight: Int,
    private val right: Int,
    private val dotSize: Dp,
    private val animation: DotIndicatorAnimation,
) : Picture() {

    @Composable
    override fun Draw(boxScope: BoxScope) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            repeat(2) { index ->
                AnimatedVisibility(
                    visible = index < 2 - (fullDotLeft - left),
                    enter = expandHorizontally(
                        animationSpec = tween(
                            durationMillis = animation.animationDuration,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = shrinkHorizontally(
                        animationSpec = tween(
                            durationMillis = animation.animationDuration,
                            easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    Row {
                        Spacer(
                            modifier = Modifier.width(spacePadding / 2)
                        )
                        Box(
                            modifier = Modifier
                                .width(dotSize)
                                .aspectRatio(1f)
                                .scale(0f)
                                .clip(CircleShape)
                        )
                        Spacer(
                            modifier = Modifier.width(spacePadding / 2)
                        )
                    }


                }

            }

            repeat(totalPage) { index ->
                AnimatedVisibility(
                    visible = index in left..right,
                    enter = expandHorizontally(
                        animationSpec = tween(
                            durationMillis = animation.animationDuration,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = shrinkHorizontally(
                        animationSpec = tween(
                            durationMillis = animation.animationDuration,
                            easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    Row {
                        Spacer(
                            modifier = Modifier.width(spacePadding / 2)
                        )
                        Box(
                            modifier = Modifier
                                .width(dotSize)
                                .aspectRatio(1f)
                                .scale(animation.getCurrentDotScale(index))
                                .clip(CircleShape)
                                .background(
                                    if (index == currentPage) {
                                        Color(0xFF0096FB)
                                    } else {
                                        Color(0xFFDADFE3)
                                    }
                                )
                        )
                        Spacer(
                            modifier = Modifier.width(spacePadding / 2)
                        )
                    }

                }
            }

            repeat(2 - (right - fullDotRight)) {
                AnimatedVisibility(
                    visible = true,
                    enter = expandHorizontally(
                        animationSpec = tween(
                            durationMillis = animation.animationDuration,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = shrinkHorizontally(
                        animationSpec = tween(
                            durationMillis = animation.animationDuration,
                            easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    Row {
                        Spacer(
                            modifier = Modifier.width(spacePadding / 2)
                        )
                        Box(
                            modifier = Modifier
                                .width(dotSize)
                                .aspectRatio(1f)
                                .scale(0f)
                                .clip(CircleShape)
                        )
                        Spacer(
                            modifier = Modifier.width(spacePadding / 2)
                        )
                    }

                }

            }
        }
    }
}