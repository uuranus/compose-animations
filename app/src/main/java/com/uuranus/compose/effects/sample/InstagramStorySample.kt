package com.uuranus.compose.effects.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uuranus.compose.effects.instagram.story.StoryPageIndicator
import com.uuranus.compose.effects.instagram.story.StoryPageTransition


@Composable
fun InstagramStorySample(modifier: Modifier = Modifier) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .padding(vertical = 24.dp)
    ) {

        val data = listOf(
            listOf("1-1", "1-2", "1-3"), listOf("2"),
            listOf("3-1", "3-2"), listOf("4-1", "4-2", "4-3", "4-4")
        )

        var firstIndex by remember {
            mutableIntStateOf(0)
        }
        var secondIndex by remember {
            mutableIntStateOf(0)
        }

        var isDragging by remember {
            mutableStateOf(false)
        }

        StoryPageTransition(
            modifier = modifier
                .fillMaxSize(),
            pageCount = data.size,
            currentPage = firstIndex,
            onDragging = { dragged ->
                isDragging = dragged
            },
            onPageChanged = {
                firstIndex = it
                secondIndex = 0
            }
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { tapOffset: Offset ->
                            val screenWidth = size.width
                            if (tapOffset.x < screenWidth / 2) {
                                if (secondIndex - 1 >= 0) {
                                    secondIndex--
                                } else {
                                    if (firstIndex == 0) return@detectTapGestures
                                    firstIndex--
                                    secondIndex = 0
                                }
                            } else {
                                if (secondIndex + 1 < data[firstIndex].size) {
                                    secondIndex++
                                } else {
                                    if (firstIndex == data.size - 1) return@detectTapGestures
                                    firstIndex++
                                    secondIndex = 0
                                }
                            }
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Gray)
                ) {

                    Text(
                        text = if (page == firstIndex) {
                            "Hello ${data[page][secondIndex]}"
                        } else {
                            "Hello ${data[page][0]}"
                        },
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = .6f),
                                blurRadius = 30f,
                            )
                        ),
                        modifier = Modifier
                            .align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )

                    StoryPageIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 12.dp, vertical = 16.dp)
                            .align(Alignment.TopCenter),
                        pageCount = data[page].size,
                        currentPage = if (page == firstIndex) {
                            secondIndex
                        } else {
                            0
                        },
                        isDragging = isDragging,
                        onPageAnimationEnded = {
                            if (secondIndex + 1 < data[firstIndex].size) {
                                secondIndex++
                            } else {
                                if (firstIndex == data.size - 1) return@StoryPageIndicator
                                firstIndex++
                                secondIndex = 0
                            }
                        }
                    )

                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color.Black)
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(6f)
                                .fillMaxHeight()
                                .background(
                                    Color.Gray.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(50)
                                )
                        )
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Custom icon",
                            tint = Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        )

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Custom icon",
                            tint = Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        )

                    }
                }
            }
        }
    }
}