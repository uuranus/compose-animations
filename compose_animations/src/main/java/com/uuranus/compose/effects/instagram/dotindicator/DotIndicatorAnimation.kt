package com.uuranus.compose.effects.instagram.dotindicator

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class DotIndicatorAnimation(
    private val totalPage: Int,
    val animationDuration: Int,
    private val left: Int,
    private val fullDotLeft: Int,
    private val fullDotRight: Int,
    private val right: Int,
) {

    private var dotScales by mutableStateOf(listOf<Float>())

    @Composable
    fun Start() {
        UpdateDotScale()
    }

    @Composable
    fun UpdateDotScale() {
        println("updatE!  $left $fullDotLeft  $fullDotRight $right")
        dotScales = List(totalPage) { index ->
            val targetValue = when {
                index in fullDotLeft..fullDotRight -> 1f
                index == fullDotLeft - 1 && index >= left -> 0.7f
                index == fullDotRight + 1 && index <= right -> 0.7f
                index == fullDotLeft - 2 && index >= left -> 0.4f
                index == fullDotRight + 2 && index <= right -> 0.4f
                else -> 0f
            }
            animateFloatAsState(
                targetValue = targetValue,
                animationSpec = tween(
                    durationMillis = animationDuration,
                    easing = FastOutSlowInEasing
                ),
                label = "dotScale"
            ).value
        }

    }

    fun getCurrentDotScale(index: Int) = dotScales[index]
}