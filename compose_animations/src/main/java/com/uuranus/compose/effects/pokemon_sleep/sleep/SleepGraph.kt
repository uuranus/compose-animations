package com.uuranus.compose.effects.pokemon_sleep.sleep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.uuranus.compose.effects.minuteDiff

@Composable
fun SleepGraph(
    sleepData: SleepData,
    yLabelsInfo: List<YLabel>,
    yLabel: @Composable (yLabel: YLabel) -> Unit,
    timeLabel: @Composable (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    val sleepTimeXLabel = @Composable {
        repeat(sleepData.hourDuration) {
            timeLabel(it)
        }
    }

    val yLabels = @Composable {
        repeat(yLabelsInfo.size) { index ->
            yLabel(yLabelsInfo[index])
        }
    }

    val rowCount = yLabelsInfo.maxOf { it.position }

    val density = LocalDensity.current

    val yAxisWidth = with(density) {
        3.dp.roundToPx()
    }

    val xAxisHeight = with(density) {
        3.dp.roundToPx()
    }

    val xTickerHeight = with(density) {
        5.dp.roundToPx()
    }

    val graphArea = @Composable {
        GraphArea(
            modifier = Modifier.fillMaxSize(),
            sleepData = sleepData,
            yLabelsInfo = yLabelsInfo,
            xLabelCount = sleepData.hourDuration,
            rowCount = rowCount,
            yAxisWidth = yAxisWidth,
            xAxisHeight = xAxisHeight,
            xAxisTickerHeight = xTickerHeight,
            yLabelPositions = yLabelsInfo.map { it.position }
        )
    }

    Layout(
        contents = listOf(sleepTimeXLabel, yLabels, graphArea),
        modifier = modifier
            .background(Color(0xFF0D63B0))
            .padding(start = 16.dp, top = 16.dp, end = 24.dp, bottom = 16.dp)
    ) {
            (sleepTimeXLabelMeasurables, yLabelsMeasurables, graphAreaMeasureable),
            constraints,
        ->

        val adjustedConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0
        )

        var yLabelMaxWidth = 0

        val labelGraphPadding = 12.dp.roundToPx()

        val sleepTimeXLabelPlaceables = sleepTimeXLabelMeasurables.map { measurable ->
            val placeable = measurable.measure(adjustedConstraints)
            placeable
        }

        val yLabelsPlaceables = yLabelsMeasurables.map { measurable ->
            val placeable = measurable.measure(adjustedConstraints)
            yLabelMaxWidth = maxOf(yLabelMaxWidth, placeable.width)
            placeable
        }

        val groupAreaWidth = constraints.maxWidth - yLabelMaxWidth - labelGraphPadding
        val groupAreaHeight =
            constraints.maxHeight - sleepTimeXLabelPlaceables.first().height - labelGraphPadding

        val groupAreaPlaceables = graphAreaMeasureable.first().measure(
            constraints.copy(
                minWidth = groupAreaWidth,
                maxWidth = groupAreaWidth,
                minHeight = groupAreaHeight,
                maxHeight = groupAreaHeight
            )
        )

        val yLabelHalfHeight = yLabelsPlaceables.first().height / 2

        val xPositionJump =
            groupAreaWidth / (sleepData.hourDuration - 1)

        //placement
        layout(constraints.maxWidth, constraints.maxHeight) {
            var xPosition = yLabelMaxWidth + labelGraphPadding
            val yPosition = constraints.maxHeight - sleepTimeXLabelPlaceables.first().height

            sleepTimeXLabelPlaceables.forEach { placeable ->
                placeable.place(x = xPosition + yAxisWidth / 2 - placeable.width / 2, y = yPosition)

                xPosition += xPositionJump
            }

            val yLabelHeight = groupAreaHeight - xTickerHeight - xAxisHeight
            val yLabelInterval =
                yLabelHeight.toFloat() / rowCount

            val yLabelBottom = yPosition - labelGraphPadding - xTickerHeight - xAxisHeight
            yLabelsPlaceables.forEachIndexed { index, placeable ->
                val yPos = yLabelsInfo[index].position * yLabelInterval
                placeable.place(
                    x = yLabelMaxWidth - placeable.width,
                    y = yLabelBottom - yPos.toInt()
                )
            }

            groupAreaPlaceables.place(
                yLabelMaxWidth + labelGraphPadding,
                yLabelHalfHeight
            )


        }

    }

}

private val sleepGradientPathColorStops: List<Pair<Float, Color>> = SleepType.entries.map {
    Pair(
        when (it) {
            SleepType.DOZE -> 0f
            SleepType.SNOOZE -> 0.5f
            SleepType.SLUMBER -> 1f
        },
        it.color
    )
}

@Composable
fun GraphArea(
    modifier: Modifier = Modifier,
    sleepData: SleepData,
    yLabelsInfo: List<YLabel>,
    xLabelCount: Int,
    rowCount: Int,
    yAxisWidth: Int,
    xAxisHeight: Int,
    xAxisTickerHeight: Int = 0,
    yAxisTickerWidth: Int = 0,
    yLabelPositions: List<Int>,
) {
    val xAxisColor = Color(0xFF6DA3DE)
    val yAxisColor = Color(0xFF6DA3DE).copy(alpha = 0.6f)

    Spacer(
        modifier = modifier
            .drawBehind {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Draw X Axis
                drawRect(
                    color = xAxisColor,
                    topLeft = Offset(0f, canvasHeight - xAxisHeight - xAxisTickerHeight),
                    size = Size(canvasWidth - yAxisTickerWidth, xAxisHeight.toFloat())
                )

                // Draw Y Axis
                drawRect(
                    color = yAxisColor,
                    topLeft = Offset(yAxisTickerWidth.toFloat(), 0f),
                    size = Size(
                        yAxisWidth.toFloat(),
                        canvasHeight - xAxisHeight - xAxisTickerHeight
                    )
                )
                drawRect(
                    color = yAxisColor,
                    topLeft = Offset(canvasWidth - yAxisWidth, 0f),
                    size = Size(
                        yAxisWidth.toFloat(),
                        canvasHeight - xAxisHeight - xAxisTickerHeight
                    )
                )

                // Draw Y Axis Guideline
                val yLabelHeight = canvasHeight - xAxisTickerHeight - xAxisHeight
                val yLabelInterval = yLabelHeight / rowCount

                yLabelPositions.forEach { position ->
                    val yPos = (rowCount - position) * yLabelInterval
                    // Align guideline with the label
                    drawRect(
                        color = yAxisColor,
                        topLeft = Offset(yAxisWidth.toFloat(), yPos.toFloat()),
                        size = Size(canvasWidth - yAxisWidth * 2, xAxisHeight.toFloat())
                    )

                    // Draw y-axis tick
                    drawRect(
                        color = yAxisColor,
                        topLeft = Offset(0f, yPos - xAxisHeight / 2),
                        size = Size(yAxisTickerWidth.toFloat(), yAxisWidth.toFloat())
                    )
                }

                // Draw X Ticks
                val xLabelInterval =
                    (canvasWidth - yAxisTickerWidth - yAxisWidth) / (xLabelCount - 1)
                for (i in 0 until xLabelCount) {
                    val xPos = i * xLabelInterval
                    drawRect(
                        color = xAxisColor,
                        topLeft = Offset(xPos, canvasHeight - xAxisTickerHeight),
                        size = Size(yAxisWidth.toFloat(), xAxisTickerHeight.toFloat())
                    )
                }
                println("canvas ${size.width}")
            }
            .drawWithCache {
                val brush = Brush.verticalGradient(
                    colorStops = sleepGradientPathColorStops.toTypedArray(),
                    startY = 0f,
                    endY = SleepType.entries.size * size.height
                )
                onDrawBehind {
                    drawSleepPath(sleepData, brush, yLabelsInfo)
                }
            }
    )

}


fun DrawScope.drawSleepPath(sleepData: SleepData, brush: Brush, yLabelsInfo: List<YLabel>) {
    println("width ${size.width} ${sleepData.minuteDuration}")
    val path = generateSleepPath(
        sleepData,
        yLabelsInfo,
        yInterval = size.height / yLabelsInfo.maxOf { it.position },
        xInterval = size.width / sleepData.endTime.minuteDiff(sleepData.startTime),
        maxHeight = size.height.toInt()
    )

    drawPath(
        path = path,
        brush = brush,
        style = Stroke(
            3.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            pathEffect = PathEffect.cornerPathEffect(
                12f
            )
        ),
    )
}

private fun generateSleepPath(
    sleepData: SleepData,
    yLabelsInfo: List<YLabel>,
    yInterval: Float,
    xInterval: Float,
    maxHeight: Int,
): Path {
    val path = Path()

    var previousPeriod: SleepPeriod? = null

    println("xInterval $xInterval")
    sleepData.periods.forEach { period ->
        val yPos = maxHeight - when (period.type) {
            SleepType.DOZE -> 50 * yInterval
            SleepType.SNOOZE -> 30 * yInterval
            SleepType.SLUMBER -> 10 * yInterval
        }

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


        println("perid $period $startXPos $endXPos")
        previousPeriod = period
    }

    return path
}

data class YLabel(
    val description: String,
    val position: Int,
)
