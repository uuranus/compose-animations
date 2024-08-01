package com.uuranus.compose.effects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeEffectsTheme {

                var isLoading by remember {
                    mutableStateOf(true)
                }
                var isLiked by remember {
                    mutableStateOf(true)
                }

                LaunchedEffect(this) {
                    delay(10000)
                    isLoading = false
                }

                val pageColors = listOf(
                    Color.Red,
                    Color.Yellow,
                    Color.Blue,
                    Color.Gray,
                    Color.Magenta,
                    Color.Green,
                    Color.Cyan,
                    Color.DarkGray,
                    Color.Green,
                    Color.Cyan,
                )

                val pageState = rememberPagerState(
                    initialPage = 0,
                    initialPageOffsetFraction = 0f,
                ) {
                    pageColors.size
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        HorizontalPager(
                            state = pageState,
                        ) { pageNum ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .background(pageColors[pageNum])
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        InstagramDotIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp),
                            currentPage = pageState.currentPage,
                            totalPage = pageState.pageCount,
                            dotSize = 10.dp,
                            spacePadding = 8.dp
                        )

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