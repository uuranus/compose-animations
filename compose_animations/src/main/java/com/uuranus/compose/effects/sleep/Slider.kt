package com.uuranus.compose.effects.sleep

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt


@Composable
fun SleepTimeGraph(
    hoursHeader: @Composable () -> Unit,
    rowCount: Int,
    dayLabel: @Composable (index: Int) -> Unit,
    sleepBar: @Composable (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    val dayLabels = @Composable {
        repeat(rowCount) { dayLabel(it) }
    }

    val sleepBars = @Composable {
        repeat(rowCount) { sleepBar(it) }
    }

    Layout(
        contents = listOf(hoursHeader, dayLabels, sleepBars),
        modifier = modifier.padding(bottom = 32.dp)
    ) {
            (hoursHeaderMeasurables, dayLabelMeasurables, sleepBarMeasureables),
            constraints,
        ->

        val hoursHeaderPlaceable = hoursHeaderMeasurables.first().measure(constraints)

        var totalHeight = hoursHeaderPlaceable.height

        val dayLabelPlaceables = dayLabelMeasurables.map { measurable ->
            val placeable = measurable.measure(constraints)
            placeable
        }

        val sleepBarPlaceables = sleepBarMeasureables.map { measurable ->
            val barParentData = measurable.parentData as TimeGraphParentData
            val barWidth = (barParentData.duration * hoursHeaderPlaceable.width).roundToInt()

            val sleepBarPlaceable = measurable.measure(
                constraints.copy(
                    minWidth = barWidth,
                    maxWidth = barWidth
                )
            )
            totalHeight += sleepBarPlaceable.height
            sleepBarPlaceable
        }

        val totalWidth = dayLabelPlaceables.first().width + hoursHeaderPlaceable.width

        //Placement
        layout(totalWidth, totalHeight) {

            val xPosition = dayLabelPlaceables.first().width
            var yPosition = hoursHeaderPlaceable.height

            hoursHeaderPlaceable.place(xPosition, 0)

            sleepBarPlaceables.forEachIndexed { index, sleepBarPlaceable ->
                val barParentData = sleepBarPlaceable.parentData as TimeGraphParentData
                val barOffset = (barParentData.offset * hoursHeaderPlaceable.width).roundToInt()

                sleepBarPlaceable.place(xPosition + barOffset, yPosition)

                val dayLabelPlaceable = dayLabelPlaceables[index]
                dayLabelPlaceable.place(0, yPosition)

                yPosition += sleepBarPlaceable.height
            }
        }
    }


}

class TimeGraphParentData(
    val duration: Float,
    val offset: Float,
) : ParentDataModifier {

    override fun Density.modifyParentData(parentData: Any?): Any? {
        return this@TimeGraphParentData
    }

}

fun Modifier.timeGraphSleepBar(
    start: LocalDateTime,
    end: LocalDateTime,
    hours: List<Int>,
): Modifier {

    val earliestTime = LocalTime.of(hours.first(), 0)
    val durationInHours = ChronoUnit.MINUTES.between(start, end) / 60f
    val durationFromEarliestToStartInHours =
        ChronoUnit.MINUTES.between(earliestTime, start.toLocalTime()) / 60f

    val offsetInHours = durationFromEarliestToStartInHours + 0.5f

    return then(
        TimeGraphParentData(
            duration = durationInHours / hours.size,
            offset = offsetInHours / hours.size
        )
    )
}

