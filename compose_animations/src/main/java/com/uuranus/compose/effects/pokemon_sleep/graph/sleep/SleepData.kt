package com.uuranus.compose.effects.pokemon_sleep.graph.sleep

import androidx.compose.ui.graphics.Color
import java.time.LocalTime

data class SleepData(
    val periods: List<SleepPeriod>,
) {
    val startTime: LocalTime = periods.firstOrNull()?.startTime ?: LocalTime.MIDNIGHT
    val endTime: LocalTime = periods.lastOrNull()?.endTime ?: LocalTime.MIDNIGHT

    private val startOver = if (startTime.minute == 0) 0 else 1
    private val endOver = if (endTime.minute == 0) 0 else 1

    val hourDuration = if (endTime.hour > startTime.hour) {
        endTime.hour - startTime.hour + startOver + endOver
    } else {
        24 - startTime.hour + endTime.hour + startOver +endOver
    }

    private val startRemainMinutes = 60 - startTime.minute
    private val endOverMinutes = endTime.minute + 1

    val minuteDuration = if (endTime.hour > startTime.hour) {
        (endTime.hour - startTime.hour + 1) * 60 + startRemainMinutes + endOverMinutes
    } else {
        (24 - startTime.hour + endTime.hour - 1) * 60 + startRemainMinutes + endOverMinutes
    }

}

data class SleepPeriod(
    val startTime: LocalTime,
    val endTime: LocalTime,
    val type: SleepType,
) {

}

enum class SleepType(val description: String, val color: Color) {
    DOZE("꾸벅꾸벅", Color(0xFFA3F8EE)),
    SNOOZE("새근새근", Color(0xFF98CFF9)),
    SLUMBER("쿨쿨", Color(0xFFA9C2FA))
}

