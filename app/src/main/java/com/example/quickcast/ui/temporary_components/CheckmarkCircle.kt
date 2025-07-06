package com.example.quickcast.ui.temporary_components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.quickcast.ui.theme.iconDark
import kotlinx.coroutines.delay


/**
 * [CheckmarkCircle] includes a custom check mark animation that is used inside snack bar messages
 *
 * @param modifier provides it is required size needed by [Canvas] to function properly.
 *@param strokeColor color of the animation icon
 * @param strokeWidth width of the animation icon
 * */


@Preview(showBackground = true)
@Composable
fun CheckmarkCircle(
    modifier: Modifier = Modifier,
    strokeColor: Color = iconDark,
    strokeWidth: Dp = 4.dp
) {
    // controls outer circle animation
    val circleProgress = remember { Animatable(0f) }
    // controls check mark animation
    val tickProgress = remember { Animatable(0f) }

    // Launch the animation
    LaunchedEffect(Unit) {
        // initial delay to let the snack bar entry animation set in
        delay(400)
        // starting outer circle animation
        circleProgress.animateTo(1f, animationSpec = tween(700, easing = LinearOutSlowInEasing))
        delay(500)
        // starting check mark animation after a slight delay
        tickProgress.animateTo(1f, animationSpec = tween(500, easing = LinearEasing))
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        // fetch minimum dimension for the canvas
        val size = size.minDimension

        // calculate radius
        val radius = size / 2

        // create an offset
        val center = Offset(size / 2, size / 2)

        // Draw circular progress
        drawArc(
            color = strokeColor,
            startAngle = -90f,
            sweepAngle = 360 * circleProgress.value,
            useCenter = false,
            size = Size(size, size),
            style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
        )

        // creating invisible path
        val path = Path().apply {
            val start = Offset(center.x - radius / 3, center.y)
            val mid = Offset(center.x - radius / 10, center.y + radius / 4)
            val end = Offset(center.x + radius / 2.5f, center.y - radius / 4)

            moveTo(start.x, start.y)
            lineTo(mid.x, mid.y)
            lineTo(end.x, end.y)
        }

        // measure the length of whole path
        val measure = PathMeasure()
        measure.setPath(path, false)

        // create an empty path
        val tickPath = Path()

        // updating the empty path as the animation moves on
        measure.getSegment(0f, measure.length * tickProgress.value, tickPath, true)

        // drawing the visible check mark
        drawPath(
            path = tickPath,
            color = strokeColor,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )
    }
}
