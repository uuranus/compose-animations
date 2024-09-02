package com.uuranus.compose.effects

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uuranus.compose.effects.pokemon_sleep.sleep.SleepGraph
import com.uuranus.compose.effects.pokemon_sleep.sleep.SleepType
import com.uuranus.compose.effects.pokemon_sleep.sleep.YLabel
import com.uuranus.compose.effects.ui.theme.ComposeEffectsTheme
import com.uuranus.compose.effects.ui.theme.LegendHeadingStyle
import com.uuranus.compose.effects.ui.theme.SmallHeadingStyle
import com.uuranus.compose.effects.ui.theme.Yellow
import com.uuranus.compose.effects.ui.theme.YellowVariant
import com.uuranus.compose.effects.youtube.PendulumEffectAnimation
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeEffectsTheme {

                var isSubscribed by remember {
                    mutableStateOf(false)
                }
                var isAndroidLiked by remember {
                    mutableStateOf(true)
                }

                var isiOSLiked by remember {
                    mutableStateOf(true)
                }

                val pageColors = listOf(
                    Color.Red,
                    Color.Yellow,
                    Color(0xff5CE1E6),
                    Color.Gray,
                    Color.Magenta,
                    Color.Green,
                    Color.Cyan,
                )

                val pageState = rememberPagerState(
                    initialPage = 0,
                    initialPageOffsetFraction = 0f,
                ) {
                    pageColors.size
                }

                val test = remember {
                    androidx.compose.animation.core.Animatable(0f)
                }

                LaunchedEffect(Unit) {
                    while (true) {
                        delay(3000)
                        isAndroidLiked = true
                        delay(6000)
                        isAndroidLiked = false
                    }
                }

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

//                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = Color(0xFF48B2FD)
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SleepGraph(
                            sleepData = sleepData,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp)
                                .aspectRatio(1.5f),
                            timeLabel = { index ->
                                if (index != 0 && index != sleepData.hourDuration - 1) {
                                    SleepTimeLabel(
                                        (sleepData.startTime.hour + index).to24Hours(),
                                        0
                                    )
                                } else if (index == 0) {
                                    SleepTimeLabel(
                                        sleepData.startTime.hour,
                                        sleepData.startTime.minute
                                    )
                                } else {
                                    SleepTimeLabel(
                                        sleepData.endTime.hour,
                                        sleepData.endTime.minute
                                    )
                                }
                            },
                            yLabelsInfo = listOf(
                                YLabel("Doz.", 50),
                                YLabel("Snooz.", 30),
                                YLabel("Slumb.", 10),
                            ),
                            yLabel = { yLabelInfo ->
                                val color = when (yLabelInfo.description) {
                                    "Doz." -> SleepType.DOZE.color
                                    "Snooz." -> SleepType.SNOOZE.color
                                    "Slumb." -> SleepType.SLUMBER.color
                                    else -> Color.Transparent
                                }

                                Text(
                                    yLabelInfo.description,
                                    modifier = Modifier
                                        .wrapContentSize(),
                                    style = LegendHeadingStyle,
                                    textAlign = TextAlign.End,
                                    color = color
                                )
                            }
                        )

                    }

                }
            }
        }
    }

}

@Composable
private fun DayLabel(dayOfWeek: DayOfWeek) {
    Text(
        dayOfWeek.getDisplayName(
            TextStyle.SHORT, Locale.getDefault()
        ),
        Modifier
            .height(24.dp)
            .padding(start = 8.dp, end = 24.dp),
        style = SmallHeadingStyle,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun DayLabel(sleepType: String) {
    Text(
        sleepType,
        Modifier
            .wrapContentSize()
            .padding(start = 8.dp, end = 24.dp),
        style = SmallHeadingStyle,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun HoursHeader(hours: List<Int>) {
    Row(
        Modifier
            .padding(bottom = 16.dp)
            .drawBehind {
                val brush = Brush.linearGradient(listOf(YellowVariant, Yellow))
                drawRoundRect(
                    brush,
                    cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx()),
                )
            }
    ) {
        hours.forEach {
            Text(
                text = "$it",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(50.dp)
                    .padding(vertical = 4.dp),
                style = SmallHeadingStyle
            )
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

