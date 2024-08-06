package com.uuranus.compose.effects

import android.content.res.Resources
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import kotlin.math.sin

data class ColorChange(
    val before: Color,
    val after: Color,
)

@Composable
fun TwitterLikeButton(
    modifier: Modifier = Modifier,
    isLiked: Boolean,
    likeColor: Color = Color(0xFFFF1B81),
    unlikeColor: Color = Color(0xFF536471),
    circleColor: ColorChange = ColorChange(Color(0xFFEB2E68), Color(0xFFD38CF1)),
    confettiColors: List<ColorChange> = getPairedColors(),
    onClick: () -> Unit,
) {

    require(confettiColors.isNotEmpty()) {
        "At least two colors are required for the confetti."
    }
    require(confettiColors.size % 2 == 0) {
        "Confetti requires an even number of colors. Please provide at least two colors."
    }

    var isHeartScaleStart by remember { mutableStateOf(false) }

    val circleSizeDuration = 200
    val heartSizeDuration = 200
    val firstConfettiScaleDuration = 500
    val secondConfettiScaleDuration = 800
    val confettiRadiusDuration = 800

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    var circleScale by remember { mutableFloatStateOf(0f) }
    var heartScale by remember { mutableFloatStateOf(0f) }
    var notLikedHeartScale by remember { mutableFloatStateOf(0f) }
    val circleColorAnimatable = remember {
        androidx.compose.animation.Animatable(circleColor.before)
    }
    var firstConfettiScale by remember { mutableFloatStateOf(1f) }
    var secondConfettiScale by remember { mutableFloatStateOf(1f) }

    var firstConfettiRadius by remember { mutableFloatStateOf(size.width.toFloat()) }
    var secondConfettiRadius by remember { mutableFloatStateOf(size.width.toFloat()) }

    val density = LocalDensity.current
    val heartSizeDp = remember {
        derivedStateOf {
            minOf(size.width, size.height).toDp()
        }
    }

    val heartSizePx by remember {
        derivedStateOf {
            with(density) {
                heartSizeDp.value.toPx()
            }
        }
    }

    val confettiRadius by remember {
        derivedStateOf {
            with(density) {
                (heartSizePx / 20f).toDp()
            }
        }
    }

    val ringWidthMax by remember {
        derivedStateOf {
            heartSizePx / 2f
        }
    }

    var ringWidth by remember {
        mutableFloatStateOf(ringWidthMax)
    }

    val ringWidthDp by remember(ringWidth) {
        derivedStateOf {
            with(density) {
                ringWidth.toDp()
            }
        }
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
            circleColorAnimatable.snapTo(circleColor.before)

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
                        stiffness = 200f
                    )
                ) { value, _ ->
                    heartScale = value
                }
            }
            launch {
                animate(
                    initialValue = ringWidthMax,
                    targetValue = 0f,
                    animationSpec = tween(heartSizeDuration, easing = LinearEasing)
                ) { value, _ ->
                    ringWidth = value
                }
            }
            launch {
                circleColorAnimatable.animateTo(
                    circleColor.after,
                    animationSpec = tween(heartSizeDuration / 2, easing = LinearEasing)
                )
            }
            launch {
                animate(
                    initialValue = 1f,
                    targetValue = 0f,
                    animationSpec = tween(firstConfettiScaleDuration, easing = LinearEasing)
                ) { value, _ ->
                    firstConfettiScale = value
                }
            }
            launch {
                animate(
                    initialValue = 1f,
                    targetValue = 0f,
                    animationSpec = tween(secondConfettiScaleDuration, easing = LinearEasing)
                ) { value, _ ->
                    secondConfettiScale = value
                }
            }
            launch {
                animate(
                    initialValue = (heartSizePx / 2) * 1.5f,
                    targetValue = (heartSizePx / 2) * 2f,
                    animationSpec = tween(confettiRadiusDuration, easing = LinearEasing)
                ) { value, _ ->
                    firstConfettiRadius = value
                }
            }
            launch {
                animate(
                    initialValue = (heartSizePx / 2) * 1.5f,
                    targetValue = (heartSizePx / 2) * 2.5f,
                    animationSpec = tween(confettiRadiusDuration, easing = LinearEasing)
                ) { value, _ ->
                    secondConfettiRadius = value
                }
            }
        } else {
            heartScale = 0f
            ringWidth = ringWidthMax
        }
    }

    Box(
        modifier = modifier
            .onGloballyPositioned {
                size = it.size
            }
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    onClick()
                })
            },
        contentAlignment = Alignment.Center
    ) {
        if (isLiked) {
            Box(
                modifier = Modifier
                    .size(heartSizeDp.value)
                    .scale(circleScale)
                    .clip(
                        shape = RingShape(ringWidth = ringWidthDp)
                    )
                    .background(
                        color = circleColorAnimatable.value
                    )
            )

            ConfettiEffect(
                size = Size(heartSizePx, heartSizePx),
                confettiRadius = confettiRadius,
                firstConfettiScale = firstConfettiScale,
                secondConfettiScale = secondConfettiScale,
                firstConfettiRadius = firstConfettiRadius,
                secondConfettiRadius = secondConfettiRadius,
                confettiColors = confettiColors,
            )

            Image(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Heart",
                colorFilter = ColorFilter.tint(likeColor),
                modifier = Modifier
                    .scale(heartScale)
                    .size(heartSizeDp.value)
            )
        } else {
            Image(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Heart",
                colorFilter = ColorFilter.tint(color = unlikeColor),
                modifier = Modifier
                    .scale(notLikedHeartScale)
                    .size(heartSizeDp.value)
            )
        }
    }
}

@Composable
private fun ConfettiEffect(
    size: Size,
    confettiRadius: Dp,
    firstConfettiScale: Float,
    secondConfettiScale: Float,
    firstConfettiRadius: Float,
    secondConfettiRadius: Float,
    confettiColors: List<ColorChange>,
) {
    val conffetiCount = confettiColors.size / 2

    val confettiDuration = 800

    val confettiOffsets =
        getConffetiAngles(conffetiCount)

    val confettiAnimatableColor = remember {
        confettiColors.map {
            androidx.compose.animation.Animatable(it.before)
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
                        confettiColors[index].after,
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
                width / 2 + (firstConfettiRadius * sin(angle)).toDp() - confettiRadius
            } else {
                width / 2 + (secondConfettiRadius * sin(angle)).toDp() - confettiRadius
            }
            val offsetY = if (index % 2 == 0) {
                height / 2 - (firstConfettiRadius * cos(angle)).toDp() - confettiRadius
            } else {
                height / 2 - (secondConfettiRadius * cos(angle)).toDp() - confettiRadius
            }

            Box(
                modifier = Modifier
                    .offset(offsetX, offsetY)
                    .scale(if (index % 2 == 0) firstConfettiScale else secondConfettiScale)
                    .size(confettiRadius * 2)
                    .clip(
                        shape = CircleShape
                    )
                    .background(confettiAnimatableColor[index].value)

            )
        }
    }
}


private fun getConffetiAngles(
    count: Int,
): List<Double> {

    val theta = PI * 2 / count

    var currentAngle = 0.0

    val list = mutableListOf<Double>()

    val diffAngle = PI / 36

    repeat(count) {

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

private fun getPairedColors(): List<ColorChange> {
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

    return image1Colors.zip(image2Colors).map {
        ColorChange(it.first, it.second)
    }
}

@Composable
fun InstagramiOSLikeButton(
    modifier: Modifier = Modifier,
    isLiked: Boolean,
    onClick: () -> Unit,
) {

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
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(
                id =
                if (isLiked) R.drawable.favorite_fill
                else R.drawable.favorite_outline
            ),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color =
                if (isLiked) Color(0xFFFF0A2F)
                else Color.Black
            ),
            modifier = Modifier
                .fillMaxSize()
                .scale(scale)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        onClick()
                    })
                }
        )


    }
}

@Composable
fun InstagramAndroidLikeButton(
    modifier: Modifier = Modifier,
    isLiked: Boolean,
    onClick: () -> Unit,
) {

    val scaleAnimatable = remember { Animatable(1f) }

    LaunchedEffect(isLiked) {
        scaleAnimatable.snapTo(0f)

        scaleAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = 0.4f,
                stiffness = 400f,
            )
        )
    }

    val scale by scaleAnimatable.asState()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = if (isLiked) R.drawable.favorite_fill else R.drawable.favorite_outline),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = if (isLiked) Color(0xFFFF0A2F) else Color.Black),
            modifier = Modifier
                .fillMaxSize()
                .scale(scale)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        onClick()
                    })
                }
        )
    }
}

private fun Double.toDp(): Dp = (this / Resources.getSystem().displayMetrics.density).dp