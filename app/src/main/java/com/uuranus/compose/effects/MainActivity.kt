package com.uuranus.compose.effects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uuranus.compose.effects.pokemon_sleep.PokemonBallShape
import com.uuranus.compose.effects.pokemon_sleep.generatePokemonBall
import com.uuranus.compose.effects.pokemon_sleep.graph.graph.YLabel
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepGraph
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepType
import com.uuranus.compose.effects.pokemon_sleep.graph.sound.SoundGraph
import com.uuranus.compose.effects.pokemon_sleep.graph.sound.SoundType
import com.uuranus.compose.effects.pokemon_sleep.graph.sound.soundDataPeriod
import com.uuranus.compose.effects.ui.theme.ComposeEffectsTheme
import com.uuranus.compose.effects.ui.theme.LegendHeadingStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    fun getPosition(row: Int, col: Int, totalColumns: Int): Pair<Int, Int> {
        // 짝수 행이면 왼쪽에서 오른쪽, 홀수 행이면 오른쪽에서 왼쪽
        val xPos = if (row % 2 == 0) col else (totalColumns - 1 - col)
        return Pair(row, xPos)
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeEffectsTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {

//                    Column(
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                    ) {

//                        Water(
//                            waterLevel = 0.5f
//                        )

//                        HorizontalPager(
//                            state = pageState,
//                            modifier = Modifier
//                                .width(200.dp)
//                                .aspectRatio(2f)
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .background(pageColors[it])
//                            )
//                        }
//
//                        Spacer(modifier = Modifier.height(32.dp))
//
//                        InstagramDotIndicator(
//                            currentPage = pageState.currentPage,
//                            totalPage = pageState.pageCount,
//                            spacePadding = 12.dp,
//                            modifier = Modifier
//                                .width(200.dp)
//                                .height(20.dp)
//
//                        )

//                        val sleepGraphData = sleepData
//                        val hours =
//                            (sleepGraphData.earliestStartHour..23) + (0..sleepGraphData.latestEndHour)
//                        val scrollState = rememberScrollState()
//
//                        SleepTimeGraph(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .horizontalScroll(scrollState)
//                                .wrapContentSize(),
//                            rowCount = sleepGraphData.sleepDayData.size,
//                            hoursHeader = {
//                                HoursHeader(hours)
//                            },
//                            dayLabel = { index ->
//                                val data = sleepGraphData.sleepDayData[index]
//                                DayLabel(data.startDate.dayOfWeek)
//                            },
//                            sleepBar = { index ->
//                                val data = sleepGraphData.sleepDayData[index]
//                                // We have access to Modifier.timeGraphBar() as we are now in TimeGraphScope
//                                SleepBar(
//                                    sleepData = data,
//                                    modifier = Modifier
//                                        .padding(bottom = 8.dp)
//                                        .timeGraphSleepBar(
//                                            start = data.firstSleepStart,
//                                            end = data.lastSleepEnd,
//                                            hours = hours,
//                                        )
//                                )
//                            },
//
//                            )

                }

                val boxSize = 50.dp
                val spacing = 45.dp

                val density = LocalDensity.current

                val xOffsetAnim = remember { Animatable(0f) }
                val yOffsetAnim = remember { Animatable(0f) }

                val scope = rememberCoroutineScope()

                val animationDuration: Int = 5000

                LaunchedEffect(Unit) {
                    scope.launch {
                        xOffsetAnim.animateTo(
                            targetValue = -5000f, // Arbitrary large value to ensure long movement
                            animationSpec = infiniteRepeatable(
                                animation = TweenSpec(
                                    durationMillis = animationDuration,
                                    easing = LinearEasing
                                ),
                                repeatMode = RepeatMode.Restart
                            )
                        )
                    }
                    scope.launch {
                        yOffsetAnim.animateTo(
                            targetValue = -5000f, // Same as above, for y-direction
                            animationSpec = infiniteRepeatable(
                                animation = TweenSpec(
                                    durationMillis = animationDuration,
                                    easing = LinearEasing
                                ),
                                repeatMode = RepeatMode.Restart
                            )
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Color(0xFF48B2FD)
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .drawWithContent {
                                val totalWidth = size.width
                                val totalHeight = size.height

                                val boxWidth = boxSize.toPx() + spacing.toPx()
                                val boxHeight = boxSize.toPx() + spacing.toPx() * 1.6f

                                val totalColumns = (totalWidth / boxWidth).roundToInt()
                                val totalRows = (totalHeight / boxHeight).roundToInt()

                                for (row in 0 until totalRows) {
                                    for (col in 0 until totalColumns) {

                                        // 각 홀수 행의 경우 xPos에 halfOffset 추가하여 지그재그 패턴을 만듦
                                        val xPos = col + if (row % 2 != 0) 0.5f else 0f

                                        val xOffset = (xPos * boxWidth + xOffsetAnim.value).let {
                                            if (it < -boxWidth) it + totalWidth + boxWidth else it
                                        }
                                        val yOffset = (row * boxHeight + yOffsetAnim.value).let {
                                            if (it < -boxHeight) it + totalHeight + boxHeight else it
                                        }

                                        // 패스를 생성하여 포켓몬 볼 모양을 그린다
                                        val path = generatePokemonBall(
                                            Size(
                                                boxSize.toPx(),
                                                boxSize.toPx()
                                            )
                                        )

                                        withTransform({
                                            translate(left = xOffset, top = yOffset)
                                        }) {
                                            drawPath(
                                                path = path,
                                                color = Color.White.copy(alpha = 0.1f),
                                            )
                                        }
                                    }
                                }
                                drawContent()
                            },
                        contentAlignment = Alignment.Center
                    ) {

//                            Box(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(32.dp)
//                                    .aspectRatio(0.8f)
//                                    .background(
//                                        Color.White,
//                                        shape = RoundedCornerShape(24.dp)
//                                    ),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                SleepGraph(
//                                    sleepData = sleepData,
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(32.dp)
//                                        .aspectRatio(1.5f),
//                                    xLabel = { index ->
//                                        if (index != 0 && index != sleepData.hourDuration - 1) {
//                                            SleepTimeLabel(
//                                                (sleepData.startTime.hour + index).to24Hours(),
//                                                0
//                                            )
//                                        } else if (index == 0) {
//                                            SleepTimeLabel(
//                                                sleepData.startTime.hour,
//                                                sleepData.startTime.minute
//                                            )
//                                        } else {
//                                            SleepTimeLabel(
//                                                sleepData.endTime.hour,
//                                                sleepData.endTime.minute
//                                            )
//                                        }
//                                    },
//                                    yLabelsInfo = listOf(
//                                        YLabel("Doz.", 50, key = SleepType.DOZE),
//                                        YLabel("Snooz.", 30, SleepType.SNOOZE),
//                                        YLabel("Slumb.", 10, SleepType.SLUMBER),
//                                    ),
//                                    yLabel = { yLabelInfo ->
//                                        val color = (yLabelInfo.key as SleepType).color
//
//                                        Text(
//                                            yLabelInfo.description,
//                                            modifier = Modifier
//                                                .wrapContentSize(),
//                                            style = LegendHeadingStyle,
//                                            textAlign = TextAlign.End,
//                                            color = color
//                                        )
//                                    }
//                                )
//                            }


                    }

                }

//                        SleepGraph(
//                            sleepData = sleepData,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(32.dp)
//                                .aspectRatio(1.5f),
//                            xLabel = { index ->
//                                if (index != 0 && index != sleepData.hourDuration - 1) {
//                                    SleepTimeLabel(
//                                        (sleepData.startTime.hour + index).to24Hours(),
//                                        0
//                                    )
//                                } else if (index == 0) {
//                                    SleepTimeLabel(
//                                        sleepData.startTime.hour,
//                                        sleepData.startTime.minute
//                                    )
//                                } else {
//                                    SleepTimeLabel(
//                                        sleepData.endTime.hour,
//                                        sleepData.endTime.minute
//                                    )
//                                }
//                            },
//                            yLabelsInfo = listOf(
//                                YLabel("Doz.", 50, key = SleepType.DOZE),
//                                YLabel("Snooz.", 30, SleepType.SNOOZE),
//                                YLabel("Slumb.", 10, SleepType.SLUMBER),
//                            ),
//                            yLabel = { yLabelInfo ->
//                                val color = (yLabelInfo.key as SleepType).color
//
//                                Text(
//                                    yLabelInfo.description,
//                                    modifier = Modifier
//                                        .wrapContentSize(),
//                                    style = LegendHeadingStyle,
//                                    textAlign = TextAlign.End,
//                                    color = color
//                                )
//                            }
//                        )

//                        SoundGraph(
//                            soundDataPeriod = soundDataPeriod,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(32.dp)
//                                .aspectRatio(1.5f),
//                            xLabel = { index ->
//                                if (index != 0 && index != soundDataPeriod.hourDuration - 1) {
//                                    SleepTimeLabel(
//                                        (soundDataPeriod.startTime.hour + index).to24Hours(),
//                                        0
//                                    )
//                                } else if (index == 0) {
//                                    SleepTimeLabel(
//                                        soundDataPeriod.startTime.hour,
//                                        soundDataPeriod.startTime.minute
//                                    )
//                                } else {
//                                    SleepTimeLabel(
//                                        soundDataPeriod.endTime.hour,
//                                        soundDataPeriod.endTime.minute
//                                    )
//                                }
//                            },
//                            yLabelsInfo = listOf(
//                                YLabel("100 dB", 100, key = SoundType.VeryLoud),
//                                YLabel("80 dB", 80, key = SoundType.Loud),
//                                YLabel("60 dB", 60, key = SoundType.Normal),
//                                YLabel("40 dB", 40, key = SoundType.Quiet),
//                            ),
//                            yLabel = { yLabelInfo ->
//                                val color = (yLabelInfo.key as SoundType).color
//
//                                Text(
//                                    yLabelInfo.description,
//                                    modifier = Modifier
//                                        .wrapContentSize(),
//                                    style = LegendHeadingStyle,
//                                    textAlign = TextAlign.End,
//                                    color = color
//                                )
//                            },
//                            maxYPosition = 100,
//                            minYPosition = 20,
//                            hideEdgeXTicker = true
//                        )

            }

        }
    }

}


@Composable
private fun SleepTimeLabel(hour: Int, minutes: Int) {
    Text(
        text = if (minutes == 0) {
            String.format("%02d", hour)
        } else {
            String.format("%02d:%02d", hour, minutes)
        },
        modifier = Modifier
            .wrapContentSize(),
        style = LegendHeadingStyle
            .copy(color = Color(0xFF93C6FA)),
        textAlign = TextAlign.Center
    )
}

