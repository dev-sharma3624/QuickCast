package com.example.quickcast.ui.screens

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class BubbleShape(private val isSentByMe : Boolean) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cornerRadius = 30f
        val tailWidth = 40f
        val tailHeight = 10f

        val rectStartCoordinates = 20f
        val rectEndX = size.width - rectStartCoordinates
        val rectEndY = size.height - rectStartCoordinates

        val path = Path().apply {
            // Main rounded rectangle
            addRoundRect(
                if(isSentByMe){
                    RoundRect(
                        rect = Rect(rectStartCoordinates, rectStartCoordinates,
                            rectEndX, rectEndY),
                        topLeft = CornerRadius(cornerRadius, cornerRadius),
                        bottomLeft = CornerRadius(cornerRadius, cornerRadius),
                        bottomRight = CornerRadius(cornerRadius, cornerRadius),
                        topRight = CornerRadius(0f, 0f)
                    )
                }else{
                    RoundRect(
                        rect = Rect(rectStartCoordinates, rectStartCoordinates,
                            rectEndX, rectEndY),
                        topLeft = CornerRadius(0f, 0f),
                        bottomLeft = CornerRadius(cornerRadius, cornerRadius),
                        bottomRight = CornerRadius(cornerRadius, cornerRadius),
                        topRight = CornerRadius(cornerRadius, cornerRadius)
                    )
                }
                
            )

            // Add tail on left or right
            if(isSentByMe){
                moveTo(rectEndX, rectStartCoordinates)
                lineTo(size.width - 10f, rectStartCoordinates)
                quadraticTo(
                    size.width ,
                    20f,
                    size.width - 10f,
                    rectStartCoordinates + 10f
                )

                quadraticTo(
                    rectEndX ,
                    rectStartCoordinates + 20f,
                    rectEndX,
                    rectStartCoordinates + 40f
                )

                close()
            }else{
                moveTo(rectStartCoordinates, rectStartCoordinates)
                lineTo(10f, rectStartCoordinates)
                quadraticTo(
                    0f ,
                    20f,
                    10f,
                    rectStartCoordinates + 10f
                )

                quadraticTo(
                    rectStartCoordinates ,
                    rectStartCoordinates + 20f,
                    rectStartCoordinates,
                    rectStartCoordinates + 40f
                )
                close()
            }
        }

        return Outline.Generic(path)
    }
}