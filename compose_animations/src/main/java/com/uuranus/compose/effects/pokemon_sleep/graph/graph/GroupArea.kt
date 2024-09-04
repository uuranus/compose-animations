package com.uuranus.compose.effects.pokemon_sleep.graph.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.math.max

@Composable
fun GraphArea(
    modifier: Modifier = Modifier,
    xLabelCount: Int,
    hideEdgeXTicker: Boolean,
    maxYPosition: Int,
    minYPosition: Int,
    yAxisWidth: Int,
    xAxisHeight: Int,
    xAxisTickerHeight: Int = 0,
    yAxisTickerWidth: Int = 0,
    yLabelPositions: List<Int>,
    content: @Composable () -> Unit,
) {
    val xAxisColor = Color(0xFF6DA3DE)
    val yAxisColor = Color(0xFF6DA3DE).copy(alpha = 0.6f)

    Box(
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

                val rowCount = maxYPosition - minYPosition

                // Draw Y Axis Guideline
                val yLabelHeight = canvasHeight - xAxisTickerHeight - xAxisHeight
                val yLabelInterval = yLabelHeight / rowCount

                yLabelPositions.forEach { position ->
                    val yPos = (rowCount - (position - minYPosition)) * yLabelInterval
                    // Align guideline with the label
                    drawRect(
                        color = yAxisColor,
                        topLeft = Offset(yAxisWidth.toFloat(), yPos),
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
                    if (hideEdgeXTicker && (i == 0 || i == xLabelCount - 1)) continue

                    val xPos = i * xLabelInterval
                    drawRect(
                        color = xAxisColor,
                        topLeft = Offset(xPos, canvasHeight - xAxisTickerHeight),
                        size = Size(yAxisWidth.toFloat(), xAxisTickerHeight.toFloat())
                    )
                }
            }

    ) {
        content()
    }
}
