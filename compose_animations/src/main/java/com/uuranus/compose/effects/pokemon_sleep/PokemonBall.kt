package com.uuranus.compose.effects.pokemon_sleep

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

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