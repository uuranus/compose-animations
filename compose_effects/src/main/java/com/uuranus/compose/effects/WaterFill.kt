package com.uuranus.compose.effects

import android.content.res.Resources
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WaterFill() {

    var contentHeight by remember {
        mutableStateOf(0.dp)
    }

    Column(modifier = Modifier.onGloballyPositioned {
        contentHeight = it.size.height.toDp()
    }, verticalArrangement = Arrangement.Bottom) {

        var targetHeight by remember {
            mutableStateOf(100.dp)
        }
        val height by animateDpAsState(
            targetValue = targetHeight,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ), label = "waterHeight"
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(Color(0xff10BBE5))
                .clickable {
                    targetHeight = contentHeight.coerceAtMost(targetHeight + 200.dp)

                    if (targetHeight == contentHeight) {
                        targetHeight = 100.dp
                    }
                }
        )
    }
}

fun Int.toDp(): Dp = (this / Resources.getSystem().displayMetrics.density).dp