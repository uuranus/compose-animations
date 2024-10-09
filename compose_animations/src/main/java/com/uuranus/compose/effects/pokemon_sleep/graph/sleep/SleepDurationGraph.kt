package com.uuranus.compose.effects.pokemon_sleep.graph.sleep

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.uuranus.compose.effects.pokemon_sleep.graph.graph.GraphArea
import com.uuranus.compose.effects.pokemon_sleep.graph.graph.YLabel

@Composable
fun SleepDurationGraph(
    modifier: Modifier = Modifier,
    xLabelCount: Int,
    xLabel: @Composable (Int) -> Unit,
    bar: @Composable (Int) -> Unit,
    barLabel: @Composable (Int) -> Unit = {},
    yLabelsInfo: List<YLabel>,
    yLabel: @Composable (yLabel: YLabel) -> Unit,
    minYPosition: Int = 0,
    maxYPosition: Int = 100,
) {

    val xLabels = @Composable {
        repeat(xLabelCount) {
            xLabel(it)
        }
    }

    val yLabels = @Composable {
        repeat(yLabelsInfo.size) { index ->
            yLabel(yLabelsInfo[index])
        }
    }

    val durationBar = @Composable {
        repeat(xLabelCount) {
            bar(it)
        }
    }

    val barLabels = @Composable {
        repeat(xLabelCount) {
            barLabel(it)
        }
    }

    val density = LocalDensity.current

    val xAxisHeight = with(density) {
        3.dp.roundToPx()
    }

    val groupArea = @Composable {
        GraphArea(
            modifier = Modifier.fillMaxSize(),
            xLabelCount = xLabelCount,
            hideEdgeXTicker = true,
            maxYPosition = maxYPosition,
            minYPosition = minYPosition,
            yAxisWidth = 0,
            xAxisHeight = xAxisHeight,
            yLabelPositions = yLabelsInfo.map { it.position }
        )
    }

    Layout(
        contents = listOf(xLabels, yLabels, groupArea, durationBar, barLabels),
        modifier = modifier
            .padding(start = 16.dp, top = 16.dp, end = 24.dp, bottom = 16.dp),
    ) {
            (xLabelsMeasurables, yLabelsMeasurables, graphAreaMeasureable, durationBarMeasureable, barLabelsMeasureable),
            constraints,
        ->
        val adjustedConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0
        )

        val labelGraphPadding = 12.dp.roundToPx()

        var totalWidth = 0
        var xLabelMaxWidth = 0
        val xLabelPlaceables = xLabelsMeasurables.map { measurable ->
            val placeable = measurable.measure(adjustedConstraints)
            totalWidth += placeable.width
            xLabelMaxWidth = maxOf(xLabelMaxWidth, placeable.width)
            placeable
        }

        var yLabelMaxWidth = 0
        val yLabelsPlaceables = yLabelsMeasurables.map { measurable ->
            val placeable = measurable.measure(adjustedConstraints)
            yLabelMaxWidth = maxOf(yLabelMaxWidth, placeable.width)
            placeable
        }

        val groupAreaWidth = constraints.maxWidth - yLabelMaxWidth - labelGraphPadding
        val groupAreaHeight =
            constraints.maxHeight - xLabelPlaceables.first().height - labelGraphPadding

        val groupAreaPlaceables = graphAreaMeasureable.first().measure(
            constraints.copy(
                minWidth = groupAreaWidth,
                maxWidth = groupAreaWidth,
                minHeight = groupAreaHeight,
                maxHeight = groupAreaHeight
            )
        )

        val xLabelSpace = (groupAreaWidth - xLabelMaxWidth * xLabelCount) / xLabelCount
        val yLabelSpace = groupAreaHeight / (maxYPosition - minYPosition - 1)

        val durationBarPlaceables = durationBarMeasureable.mapIndexed { index, measurable ->
            val barParentData = measurable.parentData as TimeGraphParentData
            val barHeight = barParentData.duration

            val placeable = measurable.measure(
                constraints.copy(
                    minWidth = xLabelPlaceables.first().width * 9 / 10,
                    maxWidth = xLabelPlaceables.first().width * 9 / 10,
                    minHeight = groupAreaHeight * barHeight / maxYPosition,
                    maxHeight = groupAreaHeight * barHeight / maxYPosition
                )
            )
            placeable
        }

        val barLabelsPlaceables = barLabelsMeasureable.map { measurable ->
            val placeable = measurable.measure(adjustedConstraints)
            placeable
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            groupAreaPlaceables.place(yLabelMaxWidth + labelGraphPadding, 0)

            val widthBaseline =
                yLabelMaxWidth + labelGraphPadding + xLabelSpace / 2
            var width =
                widthBaseline

            val heightBaseline =
                constraints.maxHeight - labelGraphPadding - xLabelPlaceables.first().height

            xLabelPlaceables.forEachIndexed { index, it ->
                it.place(
                    width + xLabelMaxWidth / 2 - it.width / 2,
                    constraints.maxHeight - it.height
                )

                val durationPlaceable = durationBarPlaceables[index]
                durationPlaceable.place(
                    width,
                    heightBaseline - durationPlaceable.height
                )

                val barLabelPlaceable = barLabelsPlaceables[index]

                val textHeight =
                    heightBaseline - durationPlaceable.height - 12 - barLabelPlaceable.height
                barLabelPlaceable.place(
                    width + durationPlaceable.width / 2 - barLabelPlaceable.width / 2,
                    textHeight
                )

                width += xLabelMaxWidth + xLabelSpace
            }

            yLabelsPlaceables.forEachIndexed { index, it ->
                val height =
                    heightBaseline - yLabelSpace * (yLabelsInfo[index].position - minYPosition)
                it.place(yLabelMaxWidth / 2 - it.width / 2, height - it.height / 2)

            }
        }

    }

}

@Stable
fun Modifier.timeGraphBar(
    duration: Int,
): Modifier {
    return then(
        TimeGraphParentData(
            duration = duration,
        )
    )
}

class TimeGraphParentData(
    val duration: Int,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): TimeGraphParentData =
        this@TimeGraphParentData
}