package com.uuranus.compose.effects

import android.content.res.Resources
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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