package com.uuranus.compose.effects

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.uuranus.variousshapes.RingShape
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TwitterHeartMotion(modifier: Modifier = Modifier) {
    var isLiked by remember { mutableStateOf(false) }
    var isHeartScaleStart by remember { mutableStateOf(false) }

    val circleSizeDuration = 1000
    val heartSizeDuration = 1000

    var size by remember {
        mutableStateOf(IntSize.Zero )
    }

    var circleScale by remember { mutableFloatStateOf(0f) }
    var heartScale by remember { mutableFloatStateOf(0f) }
    val circleColor = remember {
        androidx.compose.animation.Animatable(Color(0xFFEB2E68))
    }
    var ringWidth by remember { mutableFloatStateOf(25f) }
    var confettiAlpha by remember { mutableFloatStateOf(1f) }
    var confettiRadius by remember { mutableFloatStateOf(size.width.toFloat()) }

    LaunchedEffect(isLiked) {
        if (isLiked) {
            launch {
                animate(
                    initialValue = 0f,
                    targetValue = 1.5f,
                    animationSpec = tween(circleSizeDuration, easing = LinearEasing)
                ) { value, _ ->
                    circleScale = value

                    if (circleScale.toInt() == 1) {
                        isHeartScaleStart = true
                    }
                }
            }
            circleColor.snapTo(Color(0xFFEB2E68))
            launch {
                circleColor.animateTo(
                    Color(0xFFD38CF1),
                    animationSpec = tween(circleSizeDuration, easing = LinearEasing)
                )
            }
        } else {
            isHeartScaleStart = false
        }

    }

    LaunchedEffect(isHeartScaleStart) {
        if (isHeartScaleStart) {
            launch {
                animate(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = tween(heartSizeDuration, easing = LinearEasing)
                ) { value, _ ->
                    heartScale = value
                }
            }
            launch {
                animate(
                    initialValue = 25f,
                    targetValue = 0f,
                    animationSpec = tween(heartSizeDuration, easing = LinearEasing)
                ) { value, _ ->
                    ringWidth = value
                }
            }
            launch {
                animate(
                    initialValue = 1f,
                    targetValue = 0f,
                    animationSpec = tween(heartSizeDuration, easing = LinearEasing)
                ) { value, _ ->
                    confettiAlpha = value
                }
            }
            launch {
                animate(
                    initialValue = size.width / 2f,
                    targetValue = (size.width / 2f) * 1.5f,
                    animationSpec = tween(heartSizeDuration, easing = LinearEasing)
                ) { value, _ ->
                    confettiRadius = value
                }
            }

        } else {

            heartScale = 0f
            ringWidth = 25f
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .onGloballyPositioned {
                    size = it.size
                }
                .clickable {
                    isLiked = !isLiked
                },
            contentAlignment = Alignment.Center
        ) {
            val heartSize = with(LocalDensity.current) {
                50.dp.toPx()
            }
            if (isLiked) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .scale(circleScale)
                        .clip(
                            shape = RingShape(ringWidth = ringWidth.dp)
                        )
                        .background(
                            color = circleColor.value
                        )
                )

                ConfettiEffect(
                    size = Size(heartSize, heartSize),
                    alpha = confettiAlpha,
                    radius = confettiRadius
                )

                Image(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Heart",
                    colorFilter = ColorFilter.tint(Color(0xFFFF1B81)),
                    modifier = Modifier
                        .scale(heartScale)
                        .size(50.dp)
                )
            } else {
                Image(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Heart",
                    colorFilter = ColorFilter.tint(color = Color.DarkGray),
                    modifier = Modifier
                        .size(50.dp)
                )
            }

        }

    }

}

@Composable
fun ConfettiEffect(size: Size, alpha: Float, radius: Float) {
    val heartCount = 7

    val animatables = remember {
        mutableStateOf(getConffetiOffset(heartCount, size, radius).map {
            Animatable(it, Offset.VectorConverter)
        })
    }

    println("targetValue ${animatables.value.joinToString(" ")}")

    LaunchedEffect(Unit) {
        animatables.value.forEachIndexed { index, animatable ->
            launch {
                animatable.animateTo(
                    animatable.targetValue,
                    animationSpec = tween(1000, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    val width = with(LocalDensity.current) {
        size.width.toDp()
    }
    val height = with(LocalDensity.current) {
        size.height.toDp()
    }

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
    ) {
        animatables.value.forEach { animatable ->
            val position by animatable.asState()
            val offsetX = with(LocalDensity.current) { position.x.toDp() - 10.dp }
            val offsetY = with(LocalDensity.current) { position.y.toDp() - 10.dp }

            Box(
                modifier = Modifier
                    .offset(offsetX, offsetY)
                    .alpha(alpha)
                    .size(20.dp)
                    .drawBehind {
                        val endOffset = Offset(this.size.width, this.size.height)

                        drawCircle(
                            color = Color.Red,
                            center = Offset(endOffset.x / 3, endOffset.y / 3),
                            radius = 4.dp.toPx()
                        )

                        drawCircle(
                            color = Color.Blue,
                            center = Offset(endOffset.x * 2 / 3, endOffset.y * 2 / 3),
                            radius = 4.dp.toPx()
                        )
                    }
            ) {

            }
        }
    }
}


fun getConffetiOffset(heartCount: Int, size: Size, radius: Float): List<Offset> {

    val theta = PI * 2 / heartCount

    var currentAngle = 0.0

    val centerX = size.width / 2
    val centerY = size.height / 2

    val list = mutableListOf<Offset>()

    repeat(heartCount) {

        list.add(
            Offset(
                centerX + radius * cos(currentAngle).toFloat(),
                centerY - radius * sin(currentAngle).toFloat()
            )
        )

        currentAngle += theta
    }

    return list
}


@Composable
fun InstagramHeartMotion(modifier: Modifier = Modifier, onHeartClick: (Boolean) -> Unit) {

    var isLiked by remember {
        mutableStateOf(false)
    }

    val scaleAnimatable = remember { Animatable(1f) }

    LaunchedEffect(isLiked) {
        if (isLiked) {
            scaleAnimatable.snapTo(1f)
        } else {
            scaleAnimatable.animateTo(
                targetValue = 0.7f,
                animationSpec = tween(
                    durationMillis = 50,
                    easing = FastOutSlowInEasing
                )
            )
            scaleAnimatable.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = 1.0f,
                    stiffness = 8000f
                )
            )
        }
    }

    val scale by scaleAnimatable.asState()

    Box(
        modifier = modifier.size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = if (isLiked) R.drawable.favorite_fill else R.drawable.favorite_outline),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = if (isLiked) Color(0xFFFF0A2F) else Color.Black),
            modifier = Modifier
                .size(100.dp)
                .scale(scale)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        isLiked = !isLiked
                        onHeartClick(isLiked)
                    })
                }
        )
    }

}