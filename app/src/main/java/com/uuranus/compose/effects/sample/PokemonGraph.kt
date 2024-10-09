package com.uuranus.compose.effects.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uuranus.compose.effects.pokemon_sleep.graph.graph.YLabel
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepDurationGraph
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepGraph
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepType
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.timeGraphBar
import com.uuranus.compose.effects.pokemon_sleep.graph.sound.SoundGraph
import com.uuranus.compose.effects.pokemon_sleep.graph.sound.SoundType
import com.uuranus.compose.effects.pokemon_sleep.graph.sound.soundDataPeriod
import com.uuranus.compose.effects.to24Hours
import com.uuranus.compose.effects.ui.theme.LegendHeadingStyle

@Composable
fun SleepTypeGraphSample(
    modifier: Modifier = Modifier,
) {
    SleepGraph(
        sleepData = sleepData,
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .aspectRatio(1.5f),
        xLabel = { index ->
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
            YLabel("Doz.", 50, key = SleepType.DOZE),
            YLabel("Snooz.", 30, SleepType.SNOOZE),
            YLabel("Slumb.", 10, SleepType.SLUMBER),
        ),
        yLabel = { yLabelInfo ->
            val color = (yLabelInfo.key as SleepType).color

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

@Composable
fun SoundGraphSample(
    modifier: Modifier = Modifier,
) {
    SoundGraph(
        soundDataPeriod = soundDataPeriod,
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .aspectRatio(1.5f),
        xLabel = { index ->
            if (index != 0 && index != soundDataPeriod.hourDuration - 1) {
                SleepTimeLabel(
                    (soundDataPeriod.startTime.hour + index).to24Hours(),
                    0
                )
            } else if (index == 0) {
                SleepTimeLabel(
                    soundDataPeriod.startTime.hour,
                    soundDataPeriod.startTime.minute
                )
            } else {
                SleepTimeLabel(
                    soundDataPeriod.endTime.hour,
                    soundDataPeriod.endTime.minute
                )
            }
        },
        yLabelsInfo = listOf(
            YLabel("100 dB", 100, key = SoundType.VeryLoud),
            YLabel("80 dB", 80, key = SoundType.Loud),
            YLabel("60 dB", 60, key = SoundType.Normal),
            YLabel("40 dB", 40, key = SoundType.Quiet),
        ),
        yLabel = { yLabelInfo ->
            val color = (yLabelInfo.key as SoundType).color

            Text(
                yLabelInfo.description,
                modifier = Modifier
                    .wrapContentSize(),
                style = LegendHeadingStyle,
                textAlign = TextAlign.End,
                color = color
            )
        },
        maxYPosition = 100,
        minYPosition = 20,
        hideEdgeXTicker = true
    )
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
fun SleepDurationSample(
    modifier: Modifier = Modifier,
) {
    val sleepDurations = listOf(80, 92, 100, 80, 68, 88, 92)

    SleepDurationGraph(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .aspectRatio(1.7f)
            .background(
                Color(0xFF0089FA),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        xLabelCount = 7,
        xLabel = {
            Text(
                text = when (it) {
                    0 -> "Mon"
                    1 -> "Tue"
                    2 -> "Wed"
                    3 -> "Thu"
                    4 -> "Fri"
                    5 -> "Sat"
                    6 -> "Sun"
                    else -> ""
                },
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                ),
            )
        },
        bar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        color = Color(0xFF59F7FE),
                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                    )
                    .timeGraphBar(
                        duration = sleepDurations[it]
                    )
            )
        },
        barLabel = { index ->
            Text(
                "${sleepDurations[index]}",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF8BFCFE),
                )
            )
        },
        yLabelsInfo = listOf(
            YLabel("0", 0),
            YLabel("50", 50),
            YLabel("100", 100),
        ),
        yLabel = {
            Text(
                text = it.description,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            )
        }
    )
}