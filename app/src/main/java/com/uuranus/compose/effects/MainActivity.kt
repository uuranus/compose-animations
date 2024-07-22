package com.uuranus.compose.effects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.uuranus.compose.effects.ui.theme.ComposeEffectsTheme

enum class DrawerState {
    Closed,
    Open
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeEffectsTheme {

                var currentColor by remember {
                    mutableStateOf(Color.White)
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(12.dp),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(12.dp)
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .width(100.dp)
//                                    .height(100.dp)
//                                    .background(Color.Blue, shape = CircleShape)
//                                    .clickable {
//                                        currentColor = Color.Blue
//                                    }
//                            )
//                            Box(
//                                modifier = Modifier
//                                    .width(100.dp)
//                                    .height(100.dp)
//                                    .background(Color.Red, shape = CircleShape)
//                                    .clickable {
//                                        currentColor = Color.Red
//                                    }
//                            )
//                            Box(
//                                modifier = Modifier
//                                    .width(100.dp)
//                                    .height(100.dp)
//                                    .background(Color.Gray, shape = CircleShape)
//                                    .clickable {
//                                        currentColor = Color.Gray
//                                    }
//                            )
//                            Box(
//                                modifier = Modifier
//                                    .width(100.dp)
//                                    .height(100.dp)
//                                    .background(Color.Black, shape = CircleShape)
//                                    .clickable {
//                                        currentColor = Color.Black
//                                    }
//                            )
//                        }

                        GradientTransition()

                    }

                }
            }
        }
    }


}

fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + fraction * (stop - start)
}


@Composable
fun HomeScreenDrawer() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray)
    )
}

@Composable
fun ScreenContents(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Blue)
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeEffectsTheme {


    }
}