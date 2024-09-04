package com.uuranus.compose.effects.sleep

import androidx.compose.ui.graphics.Color
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepType
import java.time.Duration
import java.time.LocalDateTime

val Yellow_Awake = Color(0xFFffeac1)
val Yellow_Rem = Color(0xFFffdd9a)
val Yellow_Light = Color(0xFFffcb66)
val Yellow_Deep = Color(0xFFff973c)

data class SleepGraphData(
    val sleepDayData: List<SleepDayData>,
) {
    val earliestStartHour: Int by lazy {
        sleepDayData.minOf { it.firstSleepStart.hour }
    }
    val latestEndHour: Int by lazy {
        sleepDayData.maxOf { it.lastSleepEnd.hour }
    }
}

data class SleepDayData(
    val startDate: LocalDateTime,
    val sleepPeriods: List<SleepPeriod>,
    val sleepScore: Int,
) {
    val firstSleepStart: LocalDateTime by lazy {
        sleepPeriods.sortedBy(SleepPeriod::startTime).first().startTime
    }
    val lastSleepEnd: LocalDateTime by lazy {
        sleepPeriods.sortedBy(SleepPeriod::startTime).last().endTime
    }
    val totalTimeInBed: Duration by lazy {
        Duration.between(firstSleepStart, lastSleepEnd)
    }

    val sleepScoreEmoji: String by lazy {
        when (sleepScore) {
            in 0..40 -> "😖"
            in 41..60 -> "😏"
            in 60..70 -> "😴"
            in 71..100 -> "😃"
            else -> "🤷‍"
        }
    }

    fun fractionOfTotalTime(sleepPeriod: SleepPeriod): Float {
        return sleepPeriod.duration.toMinutes() / totalTimeInBed.toMinutes().toFloat()
    }

    fun minutesAfterSleepStart(sleepPeriod: SleepPeriod): Long {
        return Duration.between(
            firstSleepStart,
            sleepPeriod.startTime
        ).toMinutes()
    }
}

data class SleepPeriod(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val type: SleepType,
) {

    val duration: Duration by lazy {
        Duration.between(startTime, endTime)
    }
}

//enum class SleepType(val title: String, val color: Color) {
//    Awake("AWAKE", Yellow_Awake),
//    REM("REM", Yellow_Rem),
//    Light("LIGHT", Yellow_Light),
//    Deep("DEEP", Yellow_Deep)
//}