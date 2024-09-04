package com.uuranus.compose.effects

import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepData
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepPeriod
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepType
import java.time.LocalTime

val sleepData =
    SleepData(
        listOf(
            SleepPeriod(
                startTime = LocalTime.now()
                    .withHour(21)
                    .withMinute(8),
                endTime = LocalTime.now()
                    .withHour(21)
                    .withMinute(40),
                type = SleepType.DOZE
            ),
            SleepPeriod(
                startTime = LocalTime.now()
                    .withHour(21)
                    .withMinute(40),
                endTime = LocalTime.now()
                    .withHour(22)
                    .withMinute(20),
                type = SleepType.SNOOZE
            ),
            SleepPeriod(
                startTime = LocalTime.now()
                    .withHour(22)
                    .withMinute(20),
                endTime = LocalTime.now()
                    .withHour(22)
                    .withMinute(50),
                type = SleepType.SLUMBER
            ),
            SleepPeriod(
                startTime = LocalTime.now()
                    .withHour(22)
                    .withMinute(50),
                endTime = LocalTime.now()
                    .withHour(23)
                    .withMinute(30),
                type = SleepType.SNOOZE
            ),
            SleepPeriod(
                startTime = LocalTime.now()
                    .withHour(23)
                    .withMinute(30),
                endTime = LocalTime.now()
                    .withHour(1)
                    .withMinute(10),
                type = SleepType.SLUMBER
            ),
            SleepPeriod(
                startTime = LocalTime.now()
                    .withHour(1)
                    .withMinute(10),
                endTime = LocalTime.now()
                    .withHour(2)
                    .withMinute(30),
                type = SleepType.DOZE
            ),
            SleepPeriod(
                startTime = LocalTime.now()
                    .withHour(2)
                    .withMinute(30),
                endTime = LocalTime.now()
                    .withHour(4)
                    .withMinute(10),
                type = SleepType.SLUMBER
            ),
            SleepPeriod(
                startTime = LocalTime.now()
                    .withHour(4)
                    .withMinute(10),
                endTime = LocalTime.now()
                    .withHour(5)
                    .withMinute(30),
                type = SleepType.DOZE
            )
        )
    )