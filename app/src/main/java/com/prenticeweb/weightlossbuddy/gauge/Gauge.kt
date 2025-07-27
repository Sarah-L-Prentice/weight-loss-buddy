package com.prenticeweb.weightlossbuddy.gauge

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prenticeweb.weightlossbuddy.R
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun GaugeScreen(@FloatRange(from = 0.0, to = 45.0) currentBMI: Float) {
    val modifier = Modifier.padding(90.dp).requiredSize(300.dp)
    val underweightColour = colorResource(R.color.underweight);
    val normalColour = colorResource(R.color.normal);
    val overweightColour = colorResource(R.color.overweight);
    val obeseColour = colorResource(R.color.obese);
    val severelyObeseColour = colorResource(R.color.severelyObese);
    val morbidlyObeseColour = colorResource(R.color.morbidlyObese);
    Canvas(modifier = modifier, onDraw = {
        // Draw the arc
        val strokeWidth: Float = 90.0.dp.toPx()

        drawArc(
            color = morbidlyObeseColour,
            startAngle = 0f,
            sweepAngle = -20.4f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )

        drawArc(
            color = severelyObeseColour,
            startAngle = -20.4f,
            sweepAngle = -20.0f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
        drawArc(
            color = obeseColour,
            startAngle = -40.4f,
            sweepAngle = -20.0f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
        drawArc(
            color = overweightColour,
            startAngle = -60.4f,
            sweepAngle = -20.0f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )

        drawArc(
            color = normalColour,
            startAngle = -80.4f,
            sweepAngle = -25.6f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )

        drawArc(
            color = underweightColour,
            startAngle = -106.0f,
            sweepAngle = -74f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )

        val currentBMIOffset = pointOnCircle(
            thetaInDegrees = -90.0 - (4 * currentBMI),
            radius = size.height / 2,
            cX = center.x,
            cY = center.y
        )
        drawLine(color = Color.Black, start = center, end = currentBMIOffset, strokeWidth = 8.0f)

    })
}

private fun pointOnCircle(
    thetaInDegrees: Double,
    radius: Float,
    cX: Float,
    cY: Float,
): Offset {
    val x = cX + (radius * sin(Math.toRadians(thetaInDegrees)).toFloat())
    val y = cY + (radius * cos(Math.toRadians(thetaInDegrees)).toFloat())

    return Offset(x, y)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GaugePreview() {
    GaugeScreen(26.4f)
}
