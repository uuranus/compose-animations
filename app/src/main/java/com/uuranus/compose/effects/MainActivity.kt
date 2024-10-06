package com.uuranus.compose.effects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseInOutCirc
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.uuranus.compose.effects.instagram.story.StoryPageIndicator
import com.uuranus.compose.effects.pokemon_sleep.CircularProgress
import com.uuranus.compose.effects.sample.InstagramStorySample
import com.uuranus.compose.effects.ui.theme.ComposeEffectsTheme
import com.uuranus.compose.effects.x.Loading
import com.uuranus.compose.effects.youtube.SubscribeButton
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ComposeEffectsTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    var progress by remember { mutableFloatStateOf(0f) }
                    val max = 0.92f
                    LaunchedEffect(Unit) {
                        var time = 0.0f
                        while (time <= 1.0f) {
                            time += 0.01f
                            progress = EaseInOutCirc.transform(time) * max
                            delay(16L)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgress(
                            modifier = Modifier
                                .width(200.dp)
                                .aspectRatio(1f),
                            progress = progress

                        )

                    }

                }

            }

        }

    }
}
