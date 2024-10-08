package com.uuranus.compose.effects.pokemon_sleep.graph.sleep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.uuranus.compose.effects.pokemon_sleep.graph.graph.GraphArea
import com.uuranus.compose.effects.pokemon_sleep.graph.graph.YLabel

@Composable
fun SleepDurationGraph(
    modifier: Modifier = Modifier,
    sleepDurations: List<Int>,
    xLabel: @Composable (Int) -> Unit,
    yLabelsInfo: List<YLabel>,
    yLabel: @Composable (yLabel: YLabel) -> Unit,
    minYPosition: Int = 0,
    maxYPosition: Int = 100,
) {

    val xLabelCount = sleepDurations.size

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

    val durationBar = @Composable {
        repeat(xLabelCount) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(IntrinsicSize.Max),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("${sleepDurations[it]}",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color =  Color(0xFF8BFCFE),
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF59F7FE),
                            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                        )
                )
            }
        }
    }

    Layout(
        contents = listOf(xLabels, yLabels, groupArea, durationBar),
        modifier = modifier
            .padding(start = 16.dp, top = 16.dp, end = 24.dp, bottom = 16.dp),
    ) {
            (xLabelsMeasurables, yLabelsMeasurables, graphAreaMeasureable, durationBarMeasureable),
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
            val placeable = measurable.measure(
                constraints.copy(
                    minWidth = xLabelPlaceables.first().width * 9 / 10,
                    maxWidth = xLabelPlaceables.first().width * 9 / 10,
                    minHeight = groupAreaHeight * sleepDurations[index] / maxYPosition  ,
                    maxHeight = groupAreaHeight * sleepDurations[index] / maxYPosition
                )
            )
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

                val duration = durationBarPlaceables[index]
                duration.place(
                    width,
                    heightBaseline - duration.height
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