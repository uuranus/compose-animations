package com.uuranus.compose.effects.pokemon_sleep.graph.sound

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.uuranus.compose.effects.pokemon_sleep.graph.graph.GraphArea
import com.uuranus.compose.effects.pokemon_sleep.graph.graph.YLabel

@Composable
fun SoundGraph(
    modifier: Modifier = Modifier,
    soundDataPeriod: SoundDataPeriod,
    yLabelsInfo: List<YLabel>,
    yLabel: @Composable (yLabel: YLabel) -> Unit,
    maxYPosition: Int = 0,
    minYPosition: Int = 0,
    xLabel: @Composable (index: Int) -> Unit,
    hideEdgeXTicker: Boolean = false,
) {

    val xLabels = @Composable {
        repeat(soundDataPeriod.hourDuration) {
            xLabel(it)
        }
    }

    val yLabels = @Composable {
        repeat(yLabelsInfo.size) { index ->
            yLabel(yLabelsInfo[index])
        }
    }

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
            xLabelCount = soundDataPeriod.hourDuration,
            hideEdgeXTicker = hideEdgeXTicker,
            maxYPosition = maxYPosition,
            minYPosition = minYPosition,
            yAxisWidth = yAxisWidth,
            xAxisHeight = xAxisHeight,
            xAxisTickerHeight = xTickerHeight,
            yLabelPositions = yLabelsInfo.map { it.position }
        ) {
            SoundBar(
                soundDataPeriod,
                yLabelsInfo = yLabelsInfo,
                xAxisHeight = xAxisHeight,
                xAxisTickerHeight = xTickerHeight,
                yAxisWidth = yAxisWidth,
                yAxisTickerWidth = yAxisWidth,
                maxYPosition = maxYPosition,
                minYPosition = minYPosition,
                barWidth = yAxisWidth.toFloat()
            )
        }
    }

    Layout(
        contents = listOf(xLabels, yLabels, graphArea),
        modifier = modifier
            .background(Color(0xFF0D63B0))
            .padding(start = 16.dp, top = 16.dp, end = 24.dp, bottom = 16.dp)
    ) {
            (xLabelsMeasurables, yLabelsMeasurables, graphAreaMeasureable),
            constraints,
        ->

        val adjustedConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0
        )

        var yLabelMaxWidth = 0

        val labelGraphPadding = 12.dp.roundToPx()

        val xLabelsPlaceables = xLabelsMeasurables.map { measurable ->
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
            constraints.maxHeight - xLabelsPlaceables.first().height - labelGraphPadding

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
            groupAreaWidth / (soundDataPeriod.hourDuration - 1)

        //placement
        layout(constraints.maxWidth, constraints.maxHeight) {
            var xPosition = yLabelMaxWidth + labelGraphPadding
            val yPosition = constraints.maxHeight - xLabelsPlaceables.first().height

            xLabelsPlaceables.forEach { placeable ->
                placeable.place(x = xPosition + yAxisWidth / 2 - placeable.width / 2, y = yPosition)

                xPosition += xPositionJump
            }

            val rowCount = maxYPosition - minYPosition
            val yLabelHeight = groupAreaHeight - xTickerHeight - xAxisHeight
            val yLabelInterval =
                yLabelHeight.toFloat() / rowCount

            val yLabelBottom = yPosition - labelGraphPadding - xTickerHeight - xAxisHeight
            yLabelsPlaceables.forEachIndexed { index, placeable ->
                val yPos = (yLabelsInfo[index].position - minYPosition) * yLabelInterval
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

