package com.uuranus.compose.effects

import android.content.res.Resources
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.random.Random

internal fun Int.randomTest() = Random.nextInt(this)

internal fun Dp.dpToPx(density: Density): Int {
    return (this.value * density.density).toInt()
}

internal fun ClosedFloatingPointRange<Float>.random(): Float {
    return (Random.nextFloat() * (endInclusive - start) + start)
}

internal fun ClosedFloatingPointRange<Float>.randomRange(): ClosedFloatingPointRange<Float> {
    val start = this.random()
    val end = this.random().coerceAtLeast(start)
    return start..end
}

internal fun Int.toDp(): Dp = (this / Resources.getSystem().displayMetrics.density).dp

internal fun Double.toDp(): Dp = (this / Resources.getSystem().displayMetrics.density).dp

internal fun LocalDateTime.toHourMinutes() = "${this.hour}:${this.minute}"

fun Int.to24Hours(): Int {
    return if (this >= 24) {
        this - 24
    } else {
        this
    }
}

internal fun LocalTime.minuteDiff(from: LocalTime): Int {
    val result = if (from.hour <= this.hour) {

        val thisMinute = hour * 60 + minute
        val fromMinute = from.hour * 60 + from.minute

        thisMinute - fromMinute
    } else {

        val thisMinute = (24 + hour) * 60 + minute
        val fromMinute = from.hour * 60 + from.minute

        thisMinute - fromMinute
    }

    return result
}