package com.uuranus.compose.effects.instagram

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.uuranus.compose.effects.R
import com.uuranus.compose.effects.dpToPx
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


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

@Composable
fun InstagramLiveHeart(
    modifier: Modifier = Modifier,
) = BoxWithConstraints(modifier) {

    val density = LocalDensity.current

    val widthPx = with(density) {
        maxWidth.toPx()
    }

    val heightPx = with(density) {
        maxHeight.toPx()
    }

    var activeFirstHeartMotion by remember {
        mutableStateOf(false)
    }

    var activeHeartMotion by remember {
        mutableStateOf(false)
    }

    var firstBubbleState by remember {
        mutableStateOf(
            List(40) { index ->
                LiveHeart.create(
                    index,
                    screenSize = Size(
                        widthPx,
                        heightPx
                    ),
                    floatWidth = widthPx,
                    density = density,
                    radius = with(density) {
                        15.dp.toPx()
                    },
                    incrementationRatio = 1.5f,

                    )
            }.also {
                activeFirstHeartMotion = true
            }
        )
    }

    var bubbleState by remember {
        mutableStateOf(
            List(30) { index ->

                LiveHeart.create(
                    index,
                    screenSize = Size(
                        widthPx,
                        heightPx
                    ),
                    floatWidth = widthPx / 3f,
                    density = density,
                    radius = with(density) {
                        25.dp.toPx()
                    },
                    incrementationRatio = 0.4f,
                )
            }
        )
    }


    LaunchedEffect(activeFirstHeartMotion) {
        while (isActive) {
            awaitFrame()

            firstBubbleState = firstBubbleState.map { bubble ->
                bubble.update(
                    Size(
                        widthPx,
                        heightPx
                    ),
                    density
                )
                bubble
            }

            delay(16L)
        }
    }


    LaunchedEffect(activeFirstHeartMotion) {

        if (activeFirstHeartMotion) {
            delay(300)
            activeHeartMotion = true
        }
    }


    LaunchedEffect(activeHeartMotion) {
        if(!activeHeartMotion) return@LaunchedEffect

        while (isActive) {
            awaitFrame()
            bubbleState = bubbleState.map { bubble ->
                bubble.update(
                    Size(
                        widthPx,
                        heightPx
                    ),
                    density
                )
                bubble
            }

            delay(16L)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        for (bubble in firstBubbleState) {
            val offsetX = with(density) {
                bubble.offset.x.toDp()
            }

            val offsetY = with(density) {
                bubble.offset.y.toDp()
            }

            val radiusDp = with(density) {
                bubble.radius.toDp()
            }

            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .size(radiusDp * 2)
                    .offset(
                        x = offsetX,
                        y = offsetY
                    )
                    .alpha(
                        bubble.alpha
                    ),
                tint = Color.White.copy(alpha = bubble.backgroundAlpha)
            )
        }

        for (bubble in bubbleState) {
            val offsetX = with(density) {
                bubble.offset.x.toDp()
            }

            val offsetY = with(density) {
                bubble.offset.y.toDp()
            }

            val radiusDp = with(density) {
                bubble.radius.toDp()
            }

            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .size(radiusDp * 2)
                    .offset(
                        x = offsetX,
                        y = offsetY
                    )
                    .alpha(
                        bubble.alpha
                    ),
                tint = Color.White.copy(alpha = bubble.backgroundAlpha)
            )
        }
    }
}

class LiveHeart(
    offset: Offset,
    val radius: Float,
    val width: Float,
    angle: Double,
    val backgroundAlpha: Float,
    val index: Int,
    incrementationRatio: Float = 0.1f,
    private val startOffset: Offset = Offset.Zero,
) {

    var offset by mutableStateOf(offset)
    private var angle by mutableDoubleStateOf(angle)
    var alpha by mutableFloatStateOf(1f)

    private var range = (offset.x - width / 2)..(offset.x + width / 2)

    private val increment = radius * incrementationRatio

    private var initlaized = false

    fun update(
        screenSize: Size,
        density: Density,
    ) {

        if (offset.y >= screenSize.height + startOffset.y && initlaized) {
            return
        }

        val newX = offset.x + increment * cos(angle).toFloat()
        val newY = offset.y + increment * sin(angle).toFloat()

        offset = if (newX < 0 || newX > screenSize.width || newY < 0) {
            initializeOffset(screenSize, density)
        } else if (newX !in range) {
            angle = (3 * PI / 2 - angle) + (3 * PI / 2)
            Offset(
                x = newX + cos(angle).toFloat(),
                y = newY
            )
        } else {
            Offset(newX, newY)
        }

        if (offset.y < screenSize.height / 2) {
            alpha -= 0.05f
        }

        if (offset.y <= screenSize.height + startOffset.y) {
            initlaized = true
        }
    }

    private fun initializeOffset(
        screenSize: Size,
        density: Density,
    ): Offset {
        val x = startOffset.x + screenSize.width / 2f
        val y = startOffset.y + screenSize.height + 20.dp.dpToPx(density) * index

        alpha = 0f
        return Offset(x, y)
    }

    companion object {
        fun create(
            index: Int,
            screenSize: Size,
            density: Density,
            floatWidth: Float,
            radius: Float,
            incrementationRatio: Float = 0.4f,
            startOffset: Offset = Offset.Zero,
        ): LiveHeart {
            val x = screenSize.width / 2f + startOffset.x
            val y = screenSize.height + startOffset.y + 15.dp.dpToPx(density) * index

            return LiveHeart(
                offset = Offset(x, y),
                radius = radius,
                width = floatWidth,
                angle = Random.nextFloat() * (PI / 8) + PI * 3 / 2 - (PI * 1 / 16),
                backgroundAlpha = (Random.nextFloat() + 0.2f).coerceAtMost(1f),
                index = index,
                incrementationRatio = incrementationRatio,
                startOffset
            )
        }
    }


}
