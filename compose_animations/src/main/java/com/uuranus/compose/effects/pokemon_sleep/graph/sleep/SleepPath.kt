package com.uuranus.compose.effects.pokemon_sleep.graph.sleep

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

@Composable
internal fun SleepRoundedBar(
    sleepData: SleepData,
    yLabelsInfo: List<YLabel>,
    xAxisHeight: Int,
    xAxisTickerHeight: Int,
    yAxisWidth: Int,
    yAxisTickerWidth: Int = 0,
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

    Spacer(
        modifier = Modifier
            .fillMaxSize()
            .drawWithCache {
                val cornerRadiusStartPx = 2.dp.toPx()

                val lineThicknessPx = 3.dp.toPx()
                val roundedRectPath = Path()
                roundedRectPath.addRoundRect(
                    RoundRect(
                        rect = Rect(
                            Offset(x = 0f, y = 0f),
                            Size(
                                this.size.width,
                                this.size.height
                            )
                        ),
                    )
                )

                val roundedCornerStroke = Stroke(
                    lineThicknessPx,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                )

                val height = size.height - xAxisHeight - xAxisTickerHeight
                val width = size.width - yAxisWidth - yAxisTickerWidth

                val yInterval =
                    height / yLabelsInfo.maxOf { it.position }
                val xInterval =
                    width / sleepData.minuteDuration

                val sleepGraphPath = generateSleepPath(
                    size = Size(width, height),
                    sleepData = sleepData,
                    yLabelsInfo = yLabelsInfo,
                    yInterval = yInterval,
                    xInterval = xInterval,
                    xAxisHeight = xAxisHeight
                )
                val gradientBrush =
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to Color(0xFFDDFFD8),
                            0.3f to Color(0xFF52F5F7),
                            0.6f to Color(0xFF217BE2)
                        ),
                        startY = yInterval * 5,
                        endY = height
                    )
                val textResult = textMeasurer.measure(AnnotatedString("Hi"))

                onDrawBehind {
                    drawSleepPath(
                        roundedRectPath,
                        sleepGraphPath,
                        gradientBrush,
                        roundedCornerStroke,
                        animatedProgress,
                        textResult,
                        cornerRadiusStartPx
                    )
                }
            }
    )
}


private fun generateSleepPath(
    sleepData: SleepData,
    yLabelsInfo: List<YLabel>,
    yInterval: Float,
    xInterval: Float,
    xAxisHeight: Int,
    size: Size,
): Path {
    val path = Path()

    var previousPeriod: SleepPeriod? = null

    sleepData.periods.forEach { period ->
        var type = yLabelsInfo.find { it.key == period.type }?.position ?: 0

        if (period.type == SleepType.DOZE) type -= 5

        val yPos = size.height - type * yInterval + xAxisHeight / 2

        val startXPos =
            period.startTime.minuteDiff(sleepData.startTime).toFloat() * xInterval

        if (previousPeriod != null) {
            path.lineTo(
                x = startXPos,
                y = yPos
            )
        } else {
            path.moveTo(
                x = startXPos,
                y = yPos
            )
        }

        val endXPos = period.endTime.minuteDiff(sleepData.startTime).toFloat() * xInterval

        path.lineTo(
            x = endXPos,
            y = yPos
        )

        previousPeriod = period
    }

    return path
}

private fun DrawScope.drawSleepPath(
    roundedRectPath: Path,
    sleepGraphPath: Path,
    gradientBrush: Brush,
    roundedCornerStroke: Stroke,
    animationProgress: Float,
    textResult: TextLayoutResult,
    cornerRadiusStartPx: Float,
) {
    val pathMeasure = PathMeasure()
    pathMeasure.setPath(sleepGraphPath, false)

    val segmentPath = Path()
    var currentLength = 0f
    val pathLength = pathMeasure.length

    val segmentLength = pathLength * animationProgress

    while (currentLength < segmentLength) {
        val nextSegmentLength =
            (segmentLength - currentLength).coerceAtMost(pathLength - currentLength)

        pathMeasure.getSegment(
            currentLength,
            currentLength + nextSegmentLength,
            segmentPath,
            true
        )

        currentLength += nextSegmentLength
    }

    clipPath(roundedRectPath) {
        drawPath(
            path = segmentPath,
            style = roundedCornerStroke,
            brush = gradientBrush
        )
    }

}

