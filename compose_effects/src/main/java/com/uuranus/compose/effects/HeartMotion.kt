package com.uuranus.compose.effects

import android.content.res.Resources
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.uuranus.variousshapes.RingShape
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun TwitterHeartMotion(modifier: Modifier = Modifier) {
    var isLiked by remember { mutableStateOf(false) }
    var isHeartScaleStart by remember { mutableStateOf(false) }

    val circleSizeDuration = 200
    val heartSizeDuration = 200
    val confettiDuration = 800

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    var circleScale by remember { mutableFloatStateOf(0f) }
    var heartScale by remember { mutableFloatStateOf(0f) }
    var notLikedHeartScale by remember { mutableFloatStateOf(0f) }
    val circleColor = remember {
        androidx.compose.animation.Animatable(Color(0xFFEB2E68))
    }
    var ringWidth by remember { mutableFloatStateOf(25f) }
    var confettiScale by remember { mutableFloatStateOf(1f) }

    var firstConfettiRadius by remember { mutableFloatStateOf(size.width.toFloat()) }
    var secondConfettiRadius by remember { mutableFloatStateOf(size.width.toFloat()) }

    val heartSize = with(LocalDensity.current) {
        50.dp.toPx()
    }

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

            animate(
                initialValue = 1.5f,
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = heartSizeDuration,
                    easing = LinearEasing
                )
            ) { value, _ ->
                notLikedHeartScale = value
            }
            isHeartScaleStart = false

        }

    }

    LaunchedEffect(isHeartScaleStart) {
        if (isHeartScaleStart) {
            launch {
                animate(
                    initialValue = 0f,
                    targetValue = 0.9f,
                    animationSpec = tween(
                        durationMillis = heartSizeDuration,
                        easing = LinearEasing
                    )
                ) { value, _ ->
                    heartScale = value
                }
                animate(
                    initialValue = 0.9f,
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = 0.2f,
                        stiffness = 400f
                    )
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
                    animationSpec = tween(confettiDuration, easing = LinearEasing)
                ) { value, _ ->
                    confettiScale = value
                }
            }
            launch {
                animate(
                    initialValue = (heartSize / 2) * 1.5f,
                    targetValue = (heartSize / 2) * 2f,
                    animationSpec = tween(confettiDuration, easing = LinearEasing)
                ) { value, _ ->
                    firstConfettiRadius = value
                }
            }
            launch {
                animate(
                    initialValue = (heartSize / 2) * 1.5f,
                    targetValue = (heartSize / 2) * 2.5f,
                    animationSpec = tween(confettiDuration, easing = LinearEasing)
                ) { value, _ ->
                    secondConfettiRadius = value
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
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        isLiked = !isLiked
                    })
                },
            contentAlignment = Alignment.Center
        ) {
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
                    scale = confettiScale,
                    firstConfettiRadius = firstConfettiRadius,
                    secondConfettiRadius = secondConfettiRadius
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
                        .scale(notLikedHeartScale)
                        .size(50.dp)
                )
            }

        }

    }

}

@Composable
fun ConfettiEffect(
    size: Size,
    scale: Float,
    firstConfettiRadius: Float,
    secondConfettiRadius: Float,
) {
    val heartCount = 7

    val confettiDuration = 800

    val confettiOffsets =
        getConffetiAngles(heartCount)
    val confettiColors = getPairedColors()

    val confettiAnimatableColor = remember {
        confettiColors.map {
            androidx.compose.animation.Animatable(it.first)
        }
    }

    val width = with(LocalDensity.current) {
        size.width.toDp()
    }
    val height = with(LocalDensity.current) {
        size.height.toDp()
    }


    LaunchedEffect("confettiColor") {
        confettiAnimatableColor.forEachIndexed { index, animatable ->
            launch {
                launch {
                    animatable.animateTo(
                        confettiColors[index].second,
                        animationSpec = tween(confettiDuration, easing = EaseOut)
                    )
                }
            }
        }

    }

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
    ) {
        confettiOffsets.forEachIndexed { index, angle ->

            val offsetX = if (index % 2 == 0) {
                width / 2 + (firstConfettiRadius * sin(angle)).toDp() - 3.dp
            } else {
                width / 2 + (secondConfettiRadius * sin(angle)).toDp() - 3.dp
            }
            val offsetY = if (index % 2 == 0) {
                height / 2 - (firstConfettiRadius * cos(angle)).toDp() - 3.dp
            } else {
                height / 2 - (secondConfettiRadius * cos(angle)).toDp() - 3.dp
            }

            Box(
                modifier = Modifier
                    .offset(offsetX, offsetY)
                    .scale(scale)
                    .size(6.dp)
                    .clip(
                        shape = CircleShape
                    )
                    .background(confettiAnimatableColor[index].value)

            )
        }
    }
}


fun getConffetiAngles(
    heartCount: Int,
): List<Double> {

    val theta = PI * 2 / heartCount

    var currentAngle = 0.0

    val list = mutableListOf<Double>()

    val diffAngle = PI / 36

    repeat(heartCount) {

        list.add(
            currentAngle - diffAngle,

            )
        list.add(
            currentAngle + diffAngle,
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

fun getPairedColors(): List<Pair<Color, Color>> {
    val image1Colors = listOf(
        Color(0xFF98D6E8), // Light Blue
        Color(0xFF98D6E8), // Light Blue
        Color(0xFFE28A98), // Light Pink
        Color(0xFFE28A98), // Light Pink
        Color(0xFFB190E3), // Light Purple
        Color(0xFFB190E3), // Light Purple
        Color(0xFFA3E7B6), // Light Green
        Color(0xFFA3E7B6), // Light Green
        Color(0xFFCACBFD), // Light Purple
        Color(0xFFCACBFD), // Light Purple
        Color(0xFF8BC8F9), // Light Blue
        Color(0xFF8BC8F9), // Light Blue
        Color(0xFFDBA4F5), // Light Purple
        Color(0xFFDBA4F5)  // Light Purple
    )

    val image2Colors = listOf(
        Color(0xFF98D6E8), // Light Blue
        Color(0xFF98D6E8), // Light Blue
        Color(0xFFB190E3), // Light Purple
        Color(0xFFB190E3), // Light Purple
        Color(0xFFF2B2B3), // Light Red
        Color(0xFFF2B2B3), // Light Red
        Color(0xFFCFE0B1), // Light Green
        Color(0xFFCFE0B1), // Light Green
        Color(0xFFEDC8E3), // Light Pink
        Color(0xFFEDC8E3), // Light Pink
        Color(0xFF8CCBF3), // Light Blue
        Color(0xFF8CCBF3), // Light Blue
        Color(0xFFE5C1F5), // Light Purple
        Color(0xFFE5C1F5)  // Light Purple
    )

    return image1Colors.zip(image2Colors)
}

fun Double.toDp(): Dp = (this / Resources.getSystem().displayMetrics.density).dp