package com.uuranus.compose.effects

import android.os.Build
import android.os.Bundle
import android.util.Size
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.uuranus.compose.effects.ui.theme.ComposeEffectsTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeEffectsTheme {

                var isSubscribed by remember {
                    mutableStateOf(false)
                }
                var isAndroidLiked by remember {
                    mutableStateOf(true)
                }

                var isiOSLiked by remember {
                    mutableStateOf(true)
                }

                val pageColors = listOf(
                    Color.Red,
                    Color.Yellow,
                    Color(0xff5CE1E6),
                    Color.Gray,
                    Color.Magenta,
                    Color.Green,
                    Color.Cyan,
                )

                val pageState = rememberPagerState(
                    initialPage = 0,
                    initialPageOffsetFraction = 0f,
                ) {
                    pageColors.size
                }

                val test = remember {
                    androidx.compose.animation.core.Animatable(0f)
                }

                LaunchedEffect(Unit) {
                    test.animateTo(1f, tween(30000))
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

//                        Water(
//                            waterLevel = 0.5f
//                        )

//                        HorizontalPager(
//                            state = pageState,
//                            modifier = Modifier
//                                .width(200.dp)
//                                .aspectRatio(2f)
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .background(pageColors[it])
//                            )
//                        }
//
//                        Spacer(modifier = Modifier.height(32.dp))
//
//                        InstagramDotIndicator(
//                            currentPage = pageState.currentPage,
//                            totalPage = pageState.pageCount,
//                            spacePadding = 12.dp,
//                            modifier = Modifier
//                                .width(200.dp)
//                                .height(20.dp)
//
//                        )


                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    isSubscribed = !isSubscribed
                                })
                            }
                            .background(Color.DarkGray),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        InstagramLiveHeart(
                            isLiked = isiOSLiked,
                            modifier = Modifier
                                .padding(end = 52.dp)
                                .width(100.dp)
                                .aspectRatio(0.2f)
                                .align(Alignment.End)
                        ) {

                        }
//                        if (isSubscribed) {
//
//                            SubscribedButton(
//                                modifier = Modifier
//                                    .width(250.dp)
//                                    .aspectRatio(2f),
//                                paddingValues = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
//                            ) {
//                                isSubscribed = false
//                            }
//
//                        } else {
//                            SubscribeButton(
//                                modifier = Modifier
//                                    .width(250.dp)
//                                    .aspectRatio(2f)
//                            ) {
//                                isSubscribed = true
//
//                            }
//                        }

                    }

                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SubscribeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    var isSubsribed by remember {
        mutableStateOf(false)
    }

    var boxSize by remember {
        mutableStateOf(IntSize.Zero)
    }


    Box(
        modifier = modifier
            .background(
                color = Color(0xFF111011),
                shape = RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomStartPercent = 50,
                    bottomEndPercent = 50
                )
            )
            .onGloballyPositioned {
                val size = it.size
                boxSize = size
            }
//            .drawBehind {
//                clipRect {
//                    drawRect(
//                        brush = Brush.linearGradient(
//                            colors = listOf(
//                                Color.Transparent,
//                                Color(0xFFFB4D46),
//                                Color(0xFFE9E649),
//                                Color.Transparent,
//                            ),
//                            start = Offset(
//                                0f, 0f
//                            ),
//                            end = Offset(
//                                0f + 2 * size.width.toFloat(),
//                                size.height.toFloat()
//                            )
//                        ),
//                    )
//                }
//
//            }
            .clip(
                shape = RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomStartPercent = 50,
                    bottomEndPercent = 50
                )
            )
            .clickable {
                isSubsribed = true
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {


//        GradientShiningEffect(
//            isSubsribed,
//            size = boxSize
//        )

        Text(
            "SUBSCRIBE",
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            color = Color.White
        )
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

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val startPaddingPx =
        with(density) { paddingValues.calculateStartPadding(LayoutDirection.Ltr).toPx() }
    val endPaddingPx =
        with(density) { paddingValues.calculateEndPadding(LayoutDirection.Ltr).toPx() }

    val topPaddingPx =
        with(density) { paddingValues.calculateTopPadding().toPx() }
    val bottomPaddingPx =
        with(density) { paddingValues.calculateBottomPadding().toPx() }


    val iconSize by remember {
        derivedStateOf {

            val availableWidthPx = (size.width - startPaddingPx - endPaddingPx) / 2

            val availableHeightPx = size.height - topPaddingPx - bottomPaddingPx

            with(density) {
                minOf(availableWidthPx, availableHeightPx).toDp()
            }
        }
    }

    var endPaddingDp by remember {
        mutableStateOf(0.dp)
    }

    LaunchedEffect(Unit) {
        animate(
            initialValue = 0f,
            targetValue = endPaddingPx,
            animationSpec = tween(200),
        ) { value, _ ->
            endPaddingDp = with(density) {
                value.toDp()
            }
        }

//        isStart = true

    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                size = it.size
            }
            .background(
                color = Color(0xFFF5F2F5),
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
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            PendulumEffectAnimation(
                modifier = Modifier
                    .width(iconSize)
                    .aspectRatio(1f),
                initialAngle = 15f,
                isHanging = isStart,
                startFromInitialAngle = true
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                modifier = Modifier
                    .width(iconSize)
                    .aspectRatio(1f),
                contentDescription = null
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeEffectsTheme {

    }
}