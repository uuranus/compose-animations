package com.uuranus.compose.effects.pokemon_sleep

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.sp
import com.uuranus.compose.effects.toRadian
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularProgress(
    modifier: Modifier = Modifier,
    @FloatRange(0.0, 1.0)
    progress: Float,
    trackColor: Color = Color(0xFFF3F0F3),
    color: Color = Color(0xFF279FFD),
    textStyle: TextStyle = TextStyle(
        color = color,
        fontSize = 80.sp,
        fontWeight = FontWeight.Bold
    ),
) {

    val textMeasure = rememberTextMeasurer()

    val text = "${(progress * 100).toInt()}"

    var isDone by remember {
        mutableStateOf(false)
    }

    val fontScaleAnimatable by remember {
        mutableStateOf(Animatable(1.2f))
    }

    val fontAlphaAnimatable by remember {
        mutableStateOf(Animatable(0f))
    }

    val sparkAlphaAnimatable by remember {
        mutableStateOf(Animatable(0f))
    }

    LaunchedEffect(Unit) {
        delay(2000)
        isDone = true

        launch {
            fontScaleAnimatable.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(200)
            )

            delay(100)

            sparkAlphaAnimatable.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(200)
            )
        }

        launch {
            fontAlphaAnimatable.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(200)
            )
        }

    }


    Canvas(
        modifier = modifier
            .fillMaxSize()
    ) {
        val ringWidth = size.width / 5f

        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(ringWidth)
        )

        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = 360 * progress,
            useCenter = false,
            style = Stroke(ringWidth, cap = StrokeCap.Round)
        )

        if (isDone) {

            val style = textStyle.copy(
                fontSize = textStyle.fontSize * fontScaleAnimatable.value,
                color = color.copy(alpha = fontAlphaAnimatable.value)
            )

            val measured = textMeasure.measure(
                text = text,
                style = style,
                constraints = Constraints(
                    maxWidth = size.width.toInt() * 2 / 3,
                    maxHeight = size.height.toInt() * 2 / 3,
                )
            )

            drawText(
                textMeasurer = textMeasure,
                text = text,
                topLeft = Offset(
                    (size.width - measured.size.width) / 2,
                    (size.height - measured.size.height) / 2
                ),
                style = style
            )

            drawSparkEffect(Color(0xFFFFD356), sparkAlphaAnimatable.value)
        }


    }
}

private fun DrawScope.drawSparkEffect(color: Color, alpha: Float) {
    val numRays = 3
    val arcAngle = (-20f).toRadian()  // 각도 설정
    val longRayLength = (size.width / 2) * 0.95f
    val shortRayLength = (size.width / 2) * 0.6f

    val angleDiff = 23f.toRadian()
    val center = Offset(
        size.width / 2 + sin(arcAngle - angleDiff) * (size.width / 2),
        size.height / 2 - cos(arcAngle - angleDiff) * (size.width / 2),
    )

    val path = Path()

    val angleDistance = 5.5f.toRadian()

    for (i in 0 until numRays) {
        val angle = arcAngle - i * angleDiff

        val leftStart = Offset(
            (center.x + sin(angle - angleDistance) * longRayLength),
            (center.y - cos(angle - angleDistance) * longRayLength),
        )

        val leftEnd = Offset(
            (center.x + sin(angle - angleDistance) * shortRayLength),
            (center.y - cos(angle - angleDistance) * shortRayLength),
        )

        val rightStart = Offset(
            (center.x + sin(angle + angleDistance) * longRayLength),
            (center.y - cos(angle + angleDistance) * longRayLength),
        )

        val rightEnd = Offset(
            (center.x + sin(angle + angleDistance) * shortRayLength),
            (center.y - cos(angle + angleDistance) * shortRayLength),
        )

        path.apply {
            moveTo(leftStart.x, leftStart.y)

            lineTo(
                leftEnd.x,
                leftEnd.y
            )

            lineTo(
                rightEnd.x,
                rightEnd.y
            )

            lineTo(
                rightStart.x,
                rightStart.y
            )

            lineTo(
                leftStart.x,
                leftStart.y
            )
        }
    }

    drawPath(
        path = path,
        color = color.copy(alpha = alpha),

        )


}
