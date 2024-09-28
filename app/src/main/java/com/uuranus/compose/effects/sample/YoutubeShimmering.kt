package com.uuranus.compose.effects.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uuranus.compose.effects.youtube.ShimmeringPlaceholder


@Composable
fun YoutubeShimmering(
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF080808))
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShimmeringPlaceholder(
            modifier = Modifier
                .width(100.dp)
                .aspectRatio(1f),
            backgroundColor = MaterialTheme.colorScheme.background
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(4f)) {
            ShimmeringPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                backgroundColor = MaterialTheme.colorScheme.background
            )
            Spacer(modifier = Modifier.height(16.dp))

            ShimmeringPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                backgroundColor = MaterialTheme.colorScheme.background
            )

        }
    }

}

