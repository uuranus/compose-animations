package com.uuranus.compose.effects.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uuranus.compose.effects.instagram.InstagramDotIndicator

private val pageColors = listOf(
    Color.Gray,
    Color.Green,
    Color.Blue,
    Color.Red,
    Color.Magenta
)

@Composable
fun DotIndicatorSample(
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        val pageState = rememberPagerState {
            pageColors.size
        }

        HorizontalPager(
            state = pageState,
            modifier = Modifier
                .width(200.dp)
                .aspectRatio(2f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(pageColors[it])
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        InstagramDotIndicator(
            currentPage = pageState.currentPage,
            totalPage = pageState.pageCount,
            spacePadding = 12.dp,
            modifier = Modifier
                .width(200.dp)
                .height(20.dp)

        )
    }
}