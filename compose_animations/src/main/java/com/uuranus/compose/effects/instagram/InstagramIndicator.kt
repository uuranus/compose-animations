package com.uuranus.compose.effects.instagram

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.uuranus.compose.effects.instagram.dotindicator.DotIndicator
import com.uuranus.compose.effects.instagram.dotindicator.DotIndicatorAnimation

@Composable
fun InstagramDotIndicator(
    modifier: Modifier = Modifier,
    currentPage: Int,
    totalPage: Int,
    spacePadding: Dp,
    animationDurationMillis: Int = 600,
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
    var height by remember {
        mutableIntStateOf(20)
    }

    val maxPageDots = 7

    val density = LocalDensity.current
    val dotSize by remember(width, height) {
        derivedStateOf {
            with(density) {
                minOf(
                    ((width - spacePadding.toPx() * (maxPageDots - 1)) / maxPageDots).toDp(),
                    height.toDp()
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

    val animation = remember(left, fullDotLeft, fullDotRight, right) {
        DotIndicatorAnimation(
            totalPage,
            animationDurationMillis,
            left,
            fullDotLeft,
            fullDotRight,
            right,
        )
    }

    animation.Start()

    val dotIndicator = remember(left, fullDotLeft, fullDotRight, right, currentPage, dotSize) {
        DotIndicator(
            spacePadding,
            totalPage,
            currentPage,
            left,
            fullDotLeft,
            fullDotRight,
            right,
            dotSize,
            animation,
        )
    }

    Box(
        modifier = modifier.onGloballyPositioned {
            width = it.size.width
            height = it.size.height
        },
        contentAlignment = Alignment.Center
    ) {
        dotIndicator.Draw(boxScope = this)
    }
}