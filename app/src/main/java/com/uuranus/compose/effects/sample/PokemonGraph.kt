package com.uuranus.compose.effects.sample

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uuranus.compose.effects.pokemon_sleep.graph.graph.YLabel
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepGraph
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepType
import com.uuranus.compose.effects.pokemon_sleep.graph.sound.SoundGraph
import com.uuranus.compose.effects.pokemon_sleep.graph.sound.SoundType
import com.uuranus.compose.effects.pokemon_sleep.graph.sound.soundDataPeriod
import com.uuranus.compose.effects.to24Hours
import com.uuranus.compose.effects.ui.theme.LegendHeadingStyle

@Composable
fun SleepTypeGraphSampe(
    modifier: Modifier = Modifier
){
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

