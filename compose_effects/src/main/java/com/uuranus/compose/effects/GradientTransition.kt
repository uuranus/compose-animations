package com.uuranus.compose.effects

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
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
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
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

        Text(text = "Theme Changing", fontSize = 32.sp, fontWeight = FontWeight.Bold)
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
                        .background(color = contents[it], shape = CircleShape)
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

@Composable
fun MovingGradientBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val gradient = Brush.linearGradient(
        colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow),
        start = Offset(offset, 0f),
        end = Offset(offset + 500f, 500f)
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(brush = gradient)
    }
}