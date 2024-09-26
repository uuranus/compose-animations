package com.uuranus.compose.effects.instagram

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import kotlin.math.absoluteValue
import kotlin.math.min

private val colors = listOf(
    Color.Gray,
    Color.Green,
    Color.Blue,
    Color.Red
)

@Composable
fun StoryPageTransition(
    modifier: Modifier = Modifier,
    pages: List<Any> = listOf("1", "2", "3", "4"),
) {

    val state: PagerState = rememberPagerState(pageCount = {
        pages.size
    })

    HorizontalPager(
        state = state,
        modifier = modifier
            .fillMaxSize(),
        ) { page ->

        val pageOffset = state.offsetFromCurrentPage(page)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .graphicsLayer {
                    rotationY = pageOffset * -10f

                    transformOrigin = TransformOrigin(
                        pivotFractionX = if (pageOffset < 0f) 0f else 1f,
                        pivotFractionY = .5f
                    )
                }
                .drawWithContent {
                    this.drawContent()
                    drawRect(
                        Color.Black.copy(
                            (pageOffset.absoluteValue * 0.7f)
                        )
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors[page])
            )
            Text(
                text = "Hello $pageOffset", style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = .6f),
                        blurRadius = 30f,
                    )
                )
            )
        }
    }

}

fun PagerState.offsetFromCurrentPage(page: Int) = (currentPage - page) + currentPageOffsetFraction
