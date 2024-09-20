package com.uuranus.compose.effects.pokemon_sleep

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.delay

class PokemonBallShape : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {

        return Outline.Generic(generatePokemonBall(size))
    }
}


fun generatePokemonBall(
    size: Size,
): Path {
    val centerX = size.width / 2
    val centerY = size.height / 2

    val radius = minOf(centerX, centerY)

    val innerCircleRadius = radius * 0.5f
    val lineThickness = radius / 6

    val innerCircle = Path().apply {
        addOval(
            oval = Rect(
                center = Offset(
                    centerX, centerY
                ),
                radius = innerCircleRadius
            )
        )
    }

    val pokemonBall = Path().apply {
        addOval(
            oval = Rect(
                center = Offset(
                    centerX, centerY
                ),
                radius = radius
            )
        )

        op(
            path1 = this,
            path2 = innerCircle,
            operation = PathOperation.Difference
        )
    }


    pokemonBall.also {

        val lineBar = Path().apply {
            addRect(
                rect = Rect(
                    offset = Offset(
                        0f, centerY - lineThickness / 2
                    ),
                    size = Size(
                        size.width,
                        lineThickness
                    )
                )
            )
        }

        it.op(
            path1 = it,
            path2 = lineBar,
            operation = PathOperation.Difference
        )

    }

    pokemonBall.also {

        it.op(
            path1 = it,
            path2 = Path().apply {
                addOval(
                    oval = Rect(
                        center = Offset(
                            centerX, centerY
                        ),
                        radius = innerCircleRadius - lineThickness
                    )
                )
            },
            operation = PathOperation.Union
        )
    }

    return pokemonBall
}

@Composable
fun PokemonBallWallPaper(
    modifier: Modifier = Modifier,
) {

    val density = LocalDensity.current

    val ballSize = with(density) { 55.dp.toPx() }
    val diagonalXSpace = with(density) { 50.dp.toPx() }
    val diagonalYSpace = with(density) { 50.dp.toPx() }

    var backgroundSize by remember {
        mutableStateOf(Size.Zero)
    }

    val animationDurations = 100

    val balls by remember(backgroundSize) {
        if (backgroundSize == Size.Zero) return@remember mutableStateOf(emptyList<List<Offset>>())

        val totalWidth = backgroundSize.width
        val totalHeight = backgroundSize.height

        val offsets = mutableListOf<List<Offset>>()

        var currentY = ballSize / 2
        var rowCount = 0

        while (currentY <= totalHeight + ballSize) {

            offsets.add(
                makeNewDiagonalList(
                    rowCount,
                    currentY,
                    ballSize,
                    totalWidth,
                    diagonalXSpace
                )
            )

            currentY += ballSize + diagonalYSpace
            rowCount++
        }

        mutableStateOf(offsets.toList())
    }

    var updatedBalls by remember(balls) { mutableStateOf(balls) }

    LaunchedEffect(balls) {
        if (balls.isEmpty()) return@LaunchedEffect
        while (true) {
            val newList = updatedBalls.map { listOffsets ->
                listOffsets.map { offset ->
                    val newXOffset = offset.x - diagonalXSpace / animationDurations
                    val newYOffset = offset.y - diagonalYSpace / animationDurations

                    // Return the new updated Offset
                    offset.copy(x = newXOffset, y = newYOffset)
                }
            }

            updatedBalls = if (newList.first().first().y < -ballSize) {

                val rowCount = newList.lastIndex
                val currentY = newList.last().first().y

                newList.drop(1).plusElement(
                    makeNewDiagonalList(
                        rowCount,
                        currentY + ballSize + diagonalYSpace,
                        ballSize,
                        backgroundSize.width,
                        diagonalXSpace
                    )
                )
            } else {
                newList // No changes, continue with the updated positions
            }

            delay(16L) // Frame duration for smooth animation (~60fps)
        }
    }

    Box(
        modifier = modifier
            .onGloballyPositioned {
                backgroundSize = it.size.toSize()
            }
            .drawWithCache {

                val path = generatePokemonBall(
                    Size(
                        ballSize,
                        ballSize
                    )
                )

                onDrawBehind {
                    updatedBalls.forEach { listOffsets ->
                        listOffsets.forEach {
                            withTransform({
                                translate(left = it.x, top = it.y)
                            }) {
                                drawPath(
                                    path = path,
                                    color = Color.White.copy(alpha = 0.1f)
                                )
                            }
                        }

                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {

    }
}

private fun makeNewDiagonalList(
    rowCount: Int,
    currentY: Float,
    ballSize: Float,
    width: Float,
    diagonalXSpace: Float,
): List<Offset> {

    val rowOffsets = mutableListOf<Offset>()
    var currentX = if (rowCount % 2 == 0) -ballSize else 0f

    while (currentX <= width * 3 + ballSize) {
        rowOffsets.add(Offset(currentX, currentY))
        currentX += ballSize + diagonalXSpace
    }

    return rowOffsets
}