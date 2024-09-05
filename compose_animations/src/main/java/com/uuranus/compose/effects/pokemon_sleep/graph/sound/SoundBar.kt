package com.uuranus.compose.effects.pokemon_sleep.graph.sound

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.uuranus.compose.effects.minuteDiff
import java.time.LocalTime

@Composable
fun SoundBar(
    soundDataPeriod: SoundDataPeriod,
    xAxisHeight: Int,
    xAxisTickerHeight: Int,
    yAxisWidth: Int,
    yAxisTickerWidth: Int = 0,
    maxYPosition: Int,
    minYPosition: Int,
    barWidth: Float,
) {

    var animationProgress by remember {
        mutableFloatStateOf(0f)
    }

    val animatedProgress = List(soundDataPeriod.period.size) { index ->
        animateFloatAsState(
            targetValue = animationProgress,
            label = "progress_$index",
            animationSpec = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing,
                delayMillis = index * 50
            )
        ).value
    }

    LaunchedEffect(Unit) {
        animationProgress = 1f
    }

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
                        drawSoundBar(
                            soundData = soundData,
                            startTime = soundDataPeriod.period.first().time,
                            yInterval = yInterval,
                            xInterval = xInterval,
                            barWidth = barWidth,
                            color = soundData.type.color,
                            minYPosition = minYPosition,
                            size = Size(width, height),
                            animatedProgress = animatedProgress[index]
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
    barWidth: Float,
    size: Size,
    minYPosition: Int,
    color: Color,
    animatedProgress: Float,
) {

    val height = (soundData.decibel - minYPosition) * yInterval * animatedProgress

    val xPos = soundData.time.minuteDiff(startTime) * xInterval

    drawRect(
        color = color,
        topLeft = Offset(xPos, size.height - height),
        size = Size(barWidth, height)
    )

}
