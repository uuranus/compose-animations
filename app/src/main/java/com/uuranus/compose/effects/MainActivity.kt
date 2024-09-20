package com.uuranus.compose.effects

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.uuranus.compose.effects.instagram.InstagramLiveHeart
import com.uuranus.compose.effects.pokemon_sleep.PokemonBallWallPaper
import com.uuranus.compose.effects.pokemon_sleep.generatePokemonBall
import com.uuranus.compose.effects.pokemon_sleep.graph.graph.YLabel
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepGraph
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepType
import com.uuranus.compose.effects.ui.theme.ComposeEffectsTheme
import com.uuranus.compose.effects.ui.theme.LegendHeadingStyle
import com.uuranus.compose.effects.youtube.PendulumEffectAnimation
import com.uuranus.compose.effects.youtube.ShimmeringPlaceholder
import com.uuranus.compose.effects.youtube.SubscribeButton
import com.uuranus.compose.effects.youtube.SubscribedButton
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    fun getPosition(row: Int, col: Int, totalColumns: Int): Pair<Int, Int> {
        // 짝수 행이면 왼쪽에서 오른쪽, 홀수 행이면 오른쪽에서 왼쪽
        val xPos = if (row % 2 == 0) col else (totalColumns - 1 - col)
        return Pair(row, xPos)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ComposeEffectsTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF1BB1FB))
                    ) {
                        PokemonBallWallPaper(
                            modifier = Modifier.fillMaxSize()
                        )
                    }


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


//                Column(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {

//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        PokemonBallWallPaper(
//                            modifier = Modifier.fillMaxSize()
//                        )

//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(32.dp)
//                                .aspectRatio(0.8f)
//                                .background(
//                                    Color.White,
//                                    shape = RoundedCornerShape(24.dp)
//                                ),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            SleepGraph(
//                                sleepData = sleepData,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(32.dp)
//                                    .aspectRatio(1.5f),
//                                xLabel = { index ->
//                                    if (index != 0 && index != sleepData.hourDuration - 1) {
//                                        SleepTimeLabel(
//                                            (sleepData.startTime.hour + index).to24Hours(),
//                                            0
//                                        )
//                                    } else if (index == 0) {
//                                        SleepTimeLabel(
//                                            sleepData.startTime.hour,
//                                            sleepData.startTime.minute
//                                        )
//                                    } else {
//                                        SleepTimeLabel(
//                                            sleepData.endTime.hour,
//                                            sleepData.endTime.minute
//                                        )
//                                    }
//                                },
//                                yLabelsInfo = listOf(
//                                    YLabel("Doz.", 50, key = SleepType.DOZE),
//                                    YLabel("Snooz.", 30, SleepType.SNOOZE),
//                                    YLabel("Slumb.", 10, SleepType.SLUMBER),
//                                ),
//                                yLabel = { yLabelInfo ->
//                                    val color = (yLabelInfo.key as SleepType).color
//
//                                    Text(
//                                        yLabelInfo.description,
//                                        modifier = Modifier
//                                            .wrapContentSize(),
//                                        style = LegendHeadingStyle,
//                                        textAlign = TextAlign.End,
//                                        color = color
//                                    )
//                                }
//                            )
//                        }
//                    }


//            }

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

//                    SoundGraph(
//                        soundDataPeriod = soundDataPeriod,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(32.dp)
//                            .aspectRatio(1.5f),
//                        xLabel = { index ->
//                            if (index != 0 && index != soundDataPeriod.hourDuration - 1) {
//                                SleepTimeLabel(
//                                    (soundDataPeriod.startTime.hour + index).to24Hours(),
//                                    0
//                                )
//                            } else if (index == 0) {
//                                SleepTimeLabel(
//                                    soundDataPeriod.startTime.hour,
//                                    soundDataPeriod.startTime.minute
//                                )
//                            } else {
//                                SleepTimeLabel(
//                                    soundDataPeriod.endTime.hour,
//                                    soundDataPeriod.endTime.minute
//                                )
//                            }
//                        },
//                        yLabelsInfo = listOf(
//                            YLabel("100 dB", 100, key = SoundType.VeryLoud),
//                            YLabel("80 dB", 80, key = SoundType.Loud),
//                            YLabel("60 dB", 60, key = SoundType.Normal),
//                            YLabel("40 dB", 40, key = SoundType.Quiet),
//                        ),
//                        yLabel = { yLabelInfo ->
//                            val color = (yLabelInfo.key as SoundType).color
//
//                            Text(
//                                yLabelInfo.description,
//                                modifier = Modifier
//                                    .wrapContentSize(),
//                                style = LegendHeadingStyle,
//                                textAlign = TextAlign.End,
//                                color = color
//                            )
//                        },
//                        maxYPosition = 100,
//                        minYPosition = 20,
//                        hideEdgeXTicker = true
//                    )

//                Row(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color(0xFF080808))
//                        .padding(24.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    ShimmeringPlaceholder(
//                        modifier = Modifier
//                            .width(100.dp)
//                            .aspectRatio(1f),
//                        backgroundColor = MaterialTheme.colorScheme.background
//                    )
//
//                    Spacer(modifier = Modifier.width(16.dp))
//
//                    Column(modifier = Modifier.weight(4f)) {
//                        ShimmeringPlaceholder(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(40.dp),
//                            backgroundColor = MaterialTheme.colorScheme.background
//                        )
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        ShimmeringPlaceholder(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(40.dp),
//                            backgroundColor = MaterialTheme.colorScheme.background
//                        )
//
//                    }

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


@Composable
fun TransparentStatusBar() {
    val systemUiController = rememberSystemUiController()

    // 상태바를 투명하게 설정
    systemUiController.setStatusBarColor(
        color = Color.Transparent,  // 상태바 색을 투명하게
        darkIcons = true  // 상태바 아이콘을 어둡게 설정 (배경이 밝은 경우 사용)
    )
}