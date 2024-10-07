package com.uuranus.compose.effects.instagram.story

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.absoluteValue

private val colors = listOf(
    Color.Gray,
    Color.Green,
    Color.Blue,
    Color.Red
)

@Composable
fun StoryPageTransition(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int,
    onDragging: (Boolean) -> Unit,
    onPageChanged: (Int) -> Unit,
    content: @Composable (index: Int) -> Unit,
) {

    val state = rememberPagerState {
        pageCount
    }

    LaunchedEffect(currentPage) {
        state.animateScrollToPage(currentPage)
    }

    LaunchedEffect(state.currentPage) {
        onPageChanged(state.currentPage)
    }

    HorizontalPager(
        state = state,
        modifier = modifier
            .fillMaxSize(),
    ) { page ->

        onDragging(state.isScrollInProgress)

        val pageOffset = state.offsetFromCurrentPage(page)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationY = pageOffset * -10f

                    transformOrigin = TransformOrigin(
                        pivotFractionX = if (pageOffset < 0f) 0f else 1f,
                        pivotFractionY = 0.5f
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
            content(page)
        }
    }

}

    fun PagerState.offsetFromCurrentPage(page: Int) =
        (currentPage - page) + currentPageOffsetFraction
