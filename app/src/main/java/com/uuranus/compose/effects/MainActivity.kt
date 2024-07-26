package com.uuranus.compose.effects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uuranus.compose.effects.ui.theme.ComposeEffectsTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeEffectsTheme {

                var isLoading by remember {
                    mutableStateOf(true)
                }

                LaunchedEffect(this) {
                    delay(10000)
                    isLoading = false
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF010002)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ShimmeringEffect(isLoading = isLoading, contentAfterLoading = {
                            Text("Hello World!")
                        })
                    }

                }
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeEffectsTheme {

    }
}