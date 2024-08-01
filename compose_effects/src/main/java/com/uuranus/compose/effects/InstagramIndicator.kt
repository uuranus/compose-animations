package com.uuranus.compose.effects

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import androidx.compose.ui.unit.max

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

    val scaleCriterion = 5
    val leftScaleCriterion = 3
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

    val currentShowDots = if (totalPage <= scaleCriterion || currentPage == totalPage - 1) {
        5
    } else if (currentPage > 3 || currentPage == totalPage - 2) {
        6
    } else {
        7
    }

    val left = currentShowDots - currentPage
    val right = currentShowDots - currentPage

    val dotScales =
        List(totalPage) { index ->
            val targetValue = when {
                index < currentPage - left || index > currentPage + right -> 0f
                totalPage <= scaleCriterion -> 1f
                currentPage < leftScaleCriterion -> {
                    when {
                        (currentPage - index) > 0 -> 1f
                        (currentPage + right - index) == 0 -> 0.4f
                        (currentPage + right - index) == 1 -> 0.6f
                        else -> 1f
                    }
                }

                else -> {
                    when (currentPage - index) {
                        -2, 4 -> 0.4f
                        -1, 3 -> 0.6f
                        else -> 1f
                    }
                }
            }
            animateFloatAsState(
                targetValue = targetValue,
                animationSpec = tween(durationMillis = 200), label = "dotScale"
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

            repeat(totalPage) { index ->
                AnimatedVisibility(
                    visible = index >= currentPage - left && index <= currentPage + right,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = LinearEasing
                        )
                    ),
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 0,
                            easing = LinearEasing
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
        }
    }
}