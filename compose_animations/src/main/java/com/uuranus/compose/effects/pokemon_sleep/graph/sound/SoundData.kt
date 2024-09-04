package com.uuranus.compose.effects.pokemon_sleep.graph.sound

import androidx.compose.ui.graphics.Color
import java.time.LocalTime

data class SoundDataPeriod(
    val period: List<SoundData>,
) {
    val startTime: LocalTime = period.firstOrNull()?.time ?: LocalTime.MIDNIGHT
    val endTime: LocalTime = period.lastOrNull()?.time ?: LocalTime.MIDNIGHT

    private val endOver = if (endTime.minute == 0) 0 else 1

    val hourDuration = if (endTime.hour > startTime.hour) {
        endTime.hour - startTime.hour + endOver
    } else {
        24 - startTime.hour + endTime.hour + endOver
    }

    private val startMinutes = startTime.hour * 60 + startTime.minute
    private val endMinutes = endTime.hour * 60 + endTime.minute

    // If endMinutes is before startMinutes, it means the end time is on the next day
    val duration = if (endMinutes >= startMinutes) {
        endMinutes - startMinutes
    } else {
        (1440 - startMinutes) + endMinutes // 1440 minutes in a day
    }

    val minuteDuration = duration // Including the end time as well
}

data class SoundData(
    val time: LocalTime,
    val decibel: Int,
    val type: SoundType,
)

enum class SoundType(val description: String, val color: Color) {
    VeryLoud("매우 시끄러움", Color(0xFFFF6B68)),
    Loud("시끄러움", Color(0xFFFFA155)),
    Normal("보통", Color(0xFFFFF89E)),
    Quiet("조용함", Color(0xFFB7FEA1))
}

val soundDataPeriod = SoundDataPeriod(
    period = listOf(
        SoundData(LocalTime.of(23, 6), 85, SoundType.Loud),
        SoundData(LocalTime.of(23, 30), 65, SoundType.Normal),
        SoundData(LocalTime.of(0, 0), 40, SoundType.Quiet),
        SoundData(LocalTime.of(0, 10), 70, SoundType.Normal),
        SoundData(LocalTime.of(1, 21), 50, SoundType.Quiet),
        SoundData(LocalTime.of(1, 30), 45, SoundType.Quiet),
        SoundData(LocalTime.of(2, 0), 100, SoundType.VeryLoud),
        SoundData(LocalTime.of(2, 30), 30, SoundType.Quiet),
        SoundData(LocalTime.of(3, 10), 55, SoundType.Normal),
        SoundData(LocalTime.of(3, 36), 35, SoundType.Quiet),
        SoundData(LocalTime.of(4, 5), 80, SoundType.Loud),
        SoundData(LocalTime.of(4, 26), 75, SoundType.Loud),
        SoundData(LocalTime.of(5, 7), 60, SoundType.Normal),
        SoundData(LocalTime.of(5, 22), 65, SoundType.Normal),
        SoundData(LocalTime.of(6, 45), 92, SoundType.VeryLoud),
        SoundData(LocalTime.of(6, 55), 40, SoundType.Quiet),
        SoundData(LocalTime.of(7, 0), 55, SoundType.Normal),
        SoundData(LocalTime.of(7, 30), 50, SoundType.Quiet)
    )
)

