package com.uuranus.compose.effects

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

val OffsetToVectorConverter = TwoWayConverter<Offset, AnimationVector2D>(
    convertToVector = { AnimationVector2D(it.x, it.y) },
    convertFromVector = { Offset(it.v1, it.v2) }
)

@Composable
fun HeartButton() {
    var isLiked by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isLiked) 1.5f else 0f,
        animationSpec = spring(
            dampingRatio = 0.5f, // 낮은 값은 바운스가 더 많아짐
            stiffness = 800f // 낮은 값은 더 부드러운 움직임
        ), label = ""
    )

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .onGloballyPositioned {
                    size = it.size
                }
                .clickable {
                    isLiked = !isLiked
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Heart",
                modifier = Modifier
                    .scale(scale)
                    .size(50.dp)
            )
        }


        if (isLiked) {
            ExplodingHearts(
                size = size
            )
        }
    }

}

@Composable
fun ExplodingHearts(size: IntSize) {
    val heartCount = 6

    val animatables = remember {
        mutableStateOf(
            List(heartCount) {
                Animatable(
                    Offset(size.width / 2f, size.height / 2f),
                    OffsetToVectorConverter
                )
            }
        )
    }

    val targetValues = remember {
        mutableStateOf(getExplodingHeartOffset(heartCount, size))
    }

    LaunchedEffect(Unit) {
        animatables.value.forEachIndexed { index, animatable ->
            launch {
                delay(index * 100L)
                animatable.animateTo(
                    targetValue = targetValues.value[index],
                    animationSpec = tween(800, easing = FastOutSlowInEasing)
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

            Image(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .offset(offsetX, offsetY)
                    .size(20.dp)
            )
        }
    }
}


fun getExplodingHeartOffset(heartCount: Int, size: IntSize): List<Offset> {

    val theta = PI * 2 / heartCount

    var currentAngle = theta

    val centerX = size.width / 2
    val centerY = size.height / 2

    val radius = minOf(centerX, centerY)

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