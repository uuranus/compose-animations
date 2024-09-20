package com.uuranus.compose.effects.youtube

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SubscribeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    var isSubscribed by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {

        if (isSubscribed) {

            SubscribedButton(
                modifier = Modifier
                    .wrapContentSize()
                    .animateContentSize(),
                paddingValues = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
            ) {
                isSubscribed = false
            }

        } else {

            val backgroundColor = if (isSystemInDarkTheme()) {
                Color(0xFFF1F1F1)
            } else {
                Color(0xFF111011)
            }

            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(60.dp)
                    .animateContentSize()
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(
                            topStartPercent = 50,
                            topEndPercent = 50,
                            bottomStartPercent = 50,
                            bottomEndPercent = 50
                        )
                    )
                    .padding(
                        PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                    )
                    .clickable {
                        isSubscribed = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "SUBSCRIBE",
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSystemInDarkTheme()) Color.Black else Color.White
                )
            }
        }
    }
}

@Composable
fun SubscribedButton(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    onClick: () -> Unit,
) {

    val alpha by animateFloatAsState(targetValue = 1f, label = "")
    var isStart by remember { mutableStateOf(false) }

    val density = LocalDensity.current

    val endPaddingPx = remember {
        with(density) {
            paddingValues.calculateEndPadding(LayoutDirection.Ltr).toPx()
        }
    }

    var endPaddingDp by remember {
        mutableStateOf(0.dp)
    }

    LaunchedEffect(Unit) {
        animate(
            initialValue = 0f,
            targetValue = endPaddingPx,
            animationSpec = tween(100),
        ) { value, _ ->
            endPaddingDp = with(density) {
                value.toDp()
            }
        }

        isStart = true

    }

    val backgroundColor = if (isSystemInDarkTheme()) {
        Color(0xFF1C1C1C)
    } else {
        Color(0xFFF5F2F5)
    }

    Box(
        modifier = modifier
            .wrapContentSize()
            .animateContentSize()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomStartPercent = 50,
                    bottomEndPercent = 50
                )
            )
            .alpha(alpha)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                end = endPaddingDp
            )
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            PendulumEffectAnimation(
                modifier = Modifier
                    .width(40.dp)
                    .aspectRatio(1f),
                isHanging = isStart,
                startFromInitialAngle = true
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                modifier = Modifier
                    .height(40.dp),
                tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                contentDescription = null
            )

        }
    }
}

@Composable
fun PendulumEffectAnimation(
    modifier: Modifier = Modifier,
    initialAngle: Float = 20f,
    dampingFactor: Float = 0.6f,
    isHanging: Boolean,
    startFromInitialAngle: Boolean,
) {

    var rotation by remember {
        mutableFloatStateOf(if (startFromInitialAngle) initialAngle else 0f)
    }

    var angle by remember { mutableFloatStateOf(initialAngle) }

    var size by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(isHanging) {
        if (isHanging) {
            launch {
                while (angle >= 1f) {
                    if (startFromInitialAngle && angle == initialAngle) {
                        animate(
                            initialValue = angle,
                            targetValue = 0f,
                            animationSpec = keyframes {
                                durationMillis = 300
                                angle at 75 with LinearEasing
                                0f at 150 with LinearEasing
                                -angle at 225 with LinearEasing
                                0f at 300 with LinearEasing
                            },
                        ) { value, _ ->
                            rotation = value
                        }
                    } else {
                        animate(
                            initialValue = 0f,
                            targetValue = 0f,
                            animationSpec = keyframes {
                                durationMillis = 300
                                0f at 0 with LinearEasing
                                angle at 75 with LinearEasing
                                0f at 150 with LinearEasing
                                -angle at 225 with LinearEasing
                                0f at 300 with LinearEasing
                            },
                        ) { value, _ ->
                            rotation = value
                        }

                    }

                    angle *= dampingFactor
                }
                angle = initialAngle
            }
        } else {
            angle = initialAngle
        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                size = it.size
            },
        contentAlignment = Alignment.Center
    ) {
        val iconSize = with(LocalDensity.current) {
            minOf(size.width, size.height).toDp()
        }
        val radius = with(LocalDensity.current) {
            iconSize.toPx() / 2
        }

        Box(
            modifier = Modifier
                .size(iconSize)
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize)
                    .graphicsLayer {

                        val centerY = size.height / 2
                        val angleRadians = Math.toRadians(rotation.toDouble())

                        this.translationX = -radius * sin(angleRadians).toFloat()
                        this.translationY = centerY - radius * cos(angleRadians).toFloat()

                        this.rotationZ = rotation
                    }
            )
        }
    }
}