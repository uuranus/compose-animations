package com.uuranus.compose.effects.pokemon_sleep.graph.sound

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.uuranus.compose.effects.minuteDiff
import com.uuranus.compose.effects.pokemon_sleep.graph.graph.YLabel
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepData
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepPeriod
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepType
import java.time.LocalTime

@Composable
fun SoundBar(
    soundDataPeriod: SoundDataPeriod,
    yLabelsInfo: List<YLabel>,
    xAxisHeight: Int,
    xAxisTickerHeight: Int,
    yAxisWidth: Int,
    yAxisTickerWidth: Int = 0,
    maxYPosition: Int,
    minYPosition: Int,
    barWidth: Float,
) {
    val textMeasurer = rememberTextMeasurer()

    var animationProgress by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(Unit) {
        animationProgress = 1f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = animationProgress,
        label = "progress",
        animationSpec = tween(
            durationMillis = 3000,
            easing = FastOutSlowInEasing
        )
    )

    val barDuration = 3000 / soundDataPeriod.period.size

    Spacer(
        modifier = Modifier
            .fillMaxSize()
            .drawWithCache {

                val height = size.height - xAxisHeight - xAxisTickerHeight
                val width = size.width - yAxisWidth - yAxisTickerWidth

                val yInterval =
                    height / (maxYPosition - minYPosition)
                val xInterval =
                    width / (soundDataPeriod.minuteDuration)

                onDrawBehind {
                    soundDataPeriod.period.forEachIndexed { index, soundData ->
                        val startDelay = index * barDuration

                        drawSoundBar(
                            size = Size(width, height),
                            soundData = soundData,
                            startTime = soundDataPeriod.period.first().time,
                            yInterval = yInterval,
                            xInterval = xInterval,
                            barWidth = barWidth,
                            color = soundData.type.color,
                            minYPosition = minYPosition
                        )
                    }
                }
            }
    )
}


private fun DrawScope.drawSoundBar(
    soundData: SoundData,
    startTime: LocalTime,
    yInterval: Float,
    xInterval: Float,
    size: Size,
    barWidth: Float,
    minYPosition: Int,
    color: Color,
) {

    val height = (soundData.decibel - minYPosition) * yInterval

    val xPos = soundData.time.minuteDiff(startTime) * xInterval

    drawRect(
        color = color,
        topLeft = Offset(xPos, size.height - height),
        size = Size(barWidth, height)
    )

}
