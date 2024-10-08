package com.uuranus.compose.effects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.EaseInOutCirc
import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.uuranus.compose.effects.pokemon_sleep.CircularProgress
import com.uuranus.compose.effects.pokemon_sleep.graph.graph.YLabel
import com.uuranus.compose.effects.pokemon_sleep.graph.sleep.SleepDurationGraph
import com.uuranus.compose.effects.ui.theme.ComposeEffectsTheme
import kotlinx.coroutines.delay

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
//                        CircularProgress(
//                            modifier = Modifier
//                                .width(200.dp)
//                                .aspectRatio(1f),
//                            progress = progress
//
//                        )
                        SleepDurationGraph(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                                .aspectRatio(2f)
                                .background(
                                    Color(0xFF0089FA),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            sleepDurations = listOf(80, 92, 100, 80, 68, 88, 92),
                            xLabel = {
                                Text(
                                    text = when (it) {
                                        0 -> "Mon"
                                        1 -> "Tue"
                                        2 -> "Wed"
                                        3 -> "Thu"
                                        4 -> "Fri"
                                        5 -> "Sat"
                                        6 -> "Sun"
                                        else -> ""
                                    },
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color.White
                                    )
                                )
                            },
                            yLabelsInfo = listOf(
                                YLabel("0", 0),
                                YLabel("50", 50),
                                YLabel("100", 100),
                            ),
                            yLabel = {
                                Text(
                                    text = it.description,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color.White
                                    )
                                )
                            }
                        )
                    }

                }

            }

        }

    }
}
