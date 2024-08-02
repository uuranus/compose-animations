package com.uuranus.compose.effects

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun InstagramDotIndicator(
    modifier: Modifier = Modifier,
    currentPage: Int,
    totalPage: Int,
    dotSize: Dp = 20.dp,
    spacePadding: Dp,
) {
    require(totalPage > 0) {
        "At least 1 page is required"
    }

    require(currentPage in 0..<totalPage) {
        "currentPage is out of totalPage bounds"
    }

    var width by remember {
        mutableIntStateOf(0)
    }

    val maxPageDots = 7

    val density = LocalDensity.current
    val finalDotSize by remember {
        derivedStateOf {
            with(density) {
                minOf(
                    ((width - spacePadding.toPx() * (maxPageDots - 1)) / maxPageDots).toDp(),
                    dotSize
                )
            }
        }
    }

    var fullDotLeft by remember {
        mutableIntStateOf(0)
    }
    var fullDotRight by remember {
        mutableIntStateOf(
            if (totalPage <= 5) {
                5
            } else {
                fullDotLeft + 2
            }
        )
    }

    var left by remember {
        mutableIntStateOf(0)
    }

    var right by remember {
        mutableIntStateOf(minOf(totalPage, 5))
    }

    LaunchedEffect(currentPage) {
        if (currentPage > fullDotRight) {
            fullDotRight++
            fullDotLeft++

            if (fullDotLeft - left > 2) {
                left++
            }
            right = minOf(totalPage - 1, right + 1)
        }

        if (currentPage < fullDotLeft) {
            fullDotRight--
            fullDotLeft--

            if (right - fullDotRight > 2) {
                right--
            }
            left = maxOf(0, left - 1)
        }
    }

    val animationDuration = 100

    val dotScales = List(totalPage) { index ->
        val targetValue = when {
            index in fullDotLeft..fullDotRight -> 1f
            index == fullDotLeft - 1 && index >= left -> 0.7f
            index == fullDotRight + 1 && index <= right -> 0.7f
            index == fullDotLeft - 2 && index >= left -> 0.4f
            index == fullDotRight + 2 && index <= right -> 0.4f
            else -> 0f
        }
        animateFloatAsState(
            targetValue = targetValue,
            animationSpec = tween(durationMillis = animationDuration, easing = LinearEasing),
            label = "dotScale"
        ).value
    }

    Box(
        modifier = modifier.onGloballyPositioned {
            width = it.size.width
        },
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacePadding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            repeat(2) {
                AnimatedVisibility(
                    visible = fullDotLeft - left < 2,
                    enter = expandHorizontally(
                        animationSpec = tween(
                            durationMillis = animationDuration,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = shrinkHorizontally(
                        animationSpec = tween(
                            durationMillis = animationDuration,
                            easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    Row {
                        Box(
                            modifier = Modifier
                                .width(finalDotSize)
                                .aspectRatio(1f)
                                .scale(0f)
                                .clip(CircleShape)
                        )
                    }

                }

            }

            repeat(totalPage) { index ->

                AnimatedVisibility(
                    visible = index in left..right,
                    enter = expandHorizontally(
                        animationSpec = tween(
                            durationMillis = animationDuration,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = shrinkHorizontally(
                        animationSpec = tween(
                            durationMillis = animationDuration,
                            easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .width(finalDotSize)
                            .aspectRatio(1f)
                            .scale(dotScales[index])
                            .clip(CircleShape)
                            .background(
                                if (index == currentPage) {
                                    Color(0xFF0096FB)
                                } else {
                                    Color(0xFFDADFE3)
                                }
                            )
                    )
                }
            }

            repeat(2) {
                AnimatedVisibility(
                    visible = right - fullDotRight < 2,
                    enter = expandHorizontally(
                        animationSpec = tween(
                            durationMillis = animationDuration,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = shrinkHorizontally(
                        animationSpec = tween(
                            durationMillis = animationDuration,
                            easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    Row {
                        Box(
                            modifier = Modifier
                                .width(finalDotSize)
                                .aspectRatio(1f)
                                .scale(0f)
                                .clip(CircleShape)
                        )

                    }

                }

            }
        }
    }
}