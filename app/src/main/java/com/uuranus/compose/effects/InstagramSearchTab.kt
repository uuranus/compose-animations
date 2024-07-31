package com.uuranus.compose.effects

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat

@Composable
fun InstagramSearchTab() {
    Column {
        CustomTopAppBar(
            title = {
                Text(
                    "탐색 탭",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
            }
        )

        LazyColumn {
            items(3) {

                InstagramFeed(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}


@Composable
fun CustomTopAppBar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable (() -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 16.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (navigationIcon != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    navigationIcon()
                }
                Box(
                    modifier = Modifier.weight(5f),
                    contentAlignment = Alignment.Center
                ) {
                    title()
                }
                Spacer(modifier = Modifier.weight(1f)) // for balance
            }
        } else {
            title()
        }
    }
}

@Composable
fun InstagramFeed(modifier: Modifier = Modifier) {
    var heartCount by remember {
        mutableIntStateOf(4797)
    }

    var isLiked by remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier) {
        FeedHeader(
            modifier = Modifier.fillMaxWidth()
        )

        Image(
            painter = painterResource(id = R.drawable.cat),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )

        FeedReaction(
            isLiked = isLiked
        ) {
            isLiked = !isLiked

            heartCount = if (isLiked) heartCount + 1
            else heartCount - 1
        }

        FeedComment(modifier = Modifier.fillMaxWidth(), heartCount)
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FeedHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            tint = Color.LightGray,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text("uuranus_dev", fontWeight = FontWeight.SemiBold)
            Text("회원님을 위한 추천")
        }

        OutlinedButton(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(8.dp),

            contentPadding = PaddingValues(horizontal = 20.dp),
            border = BorderStroke(1.dp, color = Color.Black)
        ) {
            Text("팔로우", fontWeight = FontWeight.SemiBold, color = Color.Black)
        }

        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_reels_more),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = Color.Black),
            modifier = Modifier
                .size(24.dp)
        )
    }
}


@Composable
fun FeedReaction(isLiked: Boolean, onHearClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        InstagramHeartMotion(
            modifier = Modifier.size(40.dp),
            isLiked = isLiked
        ) {
            onHearClick()
        }

        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_reels_comment),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = Color.Black),
            modifier = Modifier
                .size(50.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_reels_send),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = Color.Black),
            modifier = Modifier
                .size(36.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.ic_bookmark_white),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = Color.Black),
            modifier = Modifier
                .size(30.dp)
        )
    }
}

@Composable
fun FeedComment(
    modifier: Modifier = Modifier,
    heartCount: Int,
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text("좋아요 ${formatNumberWithCommas(heartCount)}개", fontWeight = FontWeight.SemiBold)
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append("uuranus_dev")
                }
                append(" 나의 첫 게시물")
            },
        )
        Text("댓글 56개 모두 보기", color = Color.Gray)
        Text(
            "7월 29일", color = Color.Gray
        )
    }
}

fun formatNumberWithCommas(number: Int): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(number)
}