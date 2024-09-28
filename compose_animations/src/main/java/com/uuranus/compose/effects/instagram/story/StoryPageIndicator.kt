package com.uuranus.compose.effects.instagram.story

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun StoryPageIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int,
    pageDuration: Int = 3000,
    onPageAnimationEnded: () -> Unit,
    isDragging: Boolean = false,
) {
    val dotHeight = 4.dp

    val scaleAnimatable = remember(currentPage) { Animatable(0f) }

    LaunchedEffect(currentPage, isDragging) {

        if (isDragging) {
            scaleAnimatable.stop()
            return@LaunchedEffect
        }

        scaleAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = pageDuration,
                easing = LinearEasing
            )
        )

        if (scaleAnimatable.value == 1f) {
            onPageAnimationEnded()
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .height(dotHeight)
                    .weight(1f)
                    .background(
                        if (index < currentPage) {
                            Color.White.copy(alpha = 0.8f)
                        } else {
                            Color.White.copy(alpha = 0.4f)
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {

                if (index == currentPage) {
                    Box(
                        modifier = Modifier
                            .height(dotHeight)
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = scaleAnimatable.value
                                transformOrigin = TransformOrigin(0f, 0.5f)
                            }
                            .background(
                                Color.White.copy(alpha = 0.8f)
                            )
                    )
                }

            }


        }
    }

}