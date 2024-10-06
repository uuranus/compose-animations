package com.uuranus.compose.effects

import android.content.res.Resources
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.math.PI
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
    val thisMinutes = this.hour * 60 + this.minute
    val fromMinutes = from.hour * 60 + from.minute

    return if (thisMinutes >= fromMinutes) {
        thisMinutes - fromMinutes
    } else {
        (1440 - fromMinutes) + thisMinutes
    }
}

// 각도를 라디안으로 변환
internal fun Float.toRadian(): Float {
    return this * (PI.toFloat() / 180f)
}

// 라디안을 각도로 변환
internal fun Float.toDegree(): Float {
    return this * (180f / PI.toFloat())
}