package com.uuranus.compose.effects

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GradientTransition(
) {
    val contents = listOf(
        Color.Red,
        Color.Blue,
        Color.Yellow,
        Color.Gray,
        Color.Magenta
    )

    var triggerAnimation by remember { mutableStateOf(false) }

    var width by remember {
        mutableFloatStateOf(0f)
    }

    var animationProgress by remember { mutableFloatStateOf(0f) }

    var newColor by remember {
        mutableStateOf(Color.Blue)
    }

    var currentColor by remember {
        mutableStateOf(Color.Gray)
    }

    LaunchedEffect(triggerAnimation) {
        if (triggerAnimation) {
            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec =
                tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                )
            ) { value, _ ->
                animationProgress = value
            }

            triggerAnimation = false
            currentColor = newColor
            animationProgress = 0f
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {

        Text(
            text = "Theme Changing", fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(contents.size) {
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .aspectRatio(1f)
                        .background(
                            color = contents[it],
                            shape = CircleShape
                        )
                        .clickable {
                            triggerAnimation = true
                            newColor = contents[it]
                        }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 12.dp)
                .onGloballyPositioned {
                    width = it.size.width.toFloat()
                }
                .clickable { triggerAnimation = true }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(

                        Brush.horizontalGradient(
                            colorStops = arrayOf(
                                animationProgress to newColor,
                                1f to currentColor
                            ),
                            endX = (animationProgress) * width,
                            tileMode = TileMode.Clamp
                        ),
                        shape = RoundedCornerShape(12.dp)

                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("Button", modifier = Modifier.padding(16.dp))
            }

        }
    }
}


val transparent = Color.Transparent
val yellow = Color(0xFFE9E649)
val red = Color(0xFFFB4D46)


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun GradientShiningEffect(
    isShining: Boolean,
    size: IntSize,
) {

    var startX by remember { mutableFloatStateOf(-1f * size.width) }

    LaunchedEffect(size) {
        animate(
            initialValue = -2f * size.width,
            targetValue = 2f * size.width,
            animationSpec = tween(
                durationMillis = 6000,
                easing = LinearEasing
            )
        ) { value, _ ->
            startX = value
        }
    }


    val colors = List(100) { index ->
        val fraction = index / 99f
        when {
            index < 33 -> interpolateColor(
                Color.Transparent,
                red,
                (index) /99f
            )  // Transparent to Red
            index < 66 -> interpolateColor(
                red,
                yellow,
                (index -33) /66f
            )  // Red to Yellow
            else -> interpolateColor(
                yellow,
                Color.Transparent,
                (index - 66) /99f
            )  // Yellow to Transparent
        }
    }

    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(startX, 0f),
        end = Offset(startX + 2 * size.width.toFloat(), size.height.toFloat())
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = brush,
            )
    )

}

fun interpolateColor(startColor: Color, endColor: Color, fraction: Float): Color {
    val startR = startColor.red
    val startG = startColor.green
    val startB = startColor.blue
    val startA = startColor.alpha

    val endR = endColor.red
    val endG = endColor.green
    val endB = endColor.blue
    val endA = endColor.alpha

    val r = startR + (endR - startR) * fraction
    val g = startG + (endG - startG) * fraction
    val b = startB + (endB - startB) * fraction
    val a = startA + (endA - startA) * fraction

    return Color(r, g, b, a)
}