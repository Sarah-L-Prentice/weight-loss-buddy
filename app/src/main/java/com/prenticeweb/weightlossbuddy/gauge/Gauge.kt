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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val textMeasurer = rememberTextMeasurer()
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
        val textLayoutResultBMI = textMeasurer.measure(
            text = currentBMI.toString(),
            style = TextStyle(
                fontSize = 30.sp
            )
        )
        val textHeight = textLayoutResultBMI.size.height
        val textWidth = textLayoutResultBMI.size.width
        val textOffset = pointOnCircle(
            thetaInDegrees = 0.0,
            radius = size.height / 15,
            cX = center.x - textWidth / 2,
            cY = center.y - textHeight / 2
        )
        drawText(
            textLayoutResult = textLayoutResultBMI,
            topLeft = textOffset,
            color = Color.Black
        )

        val textLayoutResultCategory = textMeasurer.measure(
            text = getCategorization(currentBMI),
            style = TextStyle(
                fontSize = 30.sp
            )
        )
        val textHeightCategory = textLayoutResultCategory.size.height
        val textWidthCategory = textLayoutResultCategory.size.width
        val textOffsetCategory = pointOnCircle(
            thetaInDegrees = 0.0,
            radius = size.height / 5,
            cX = center.x - textWidthCategory / 2,
            cY = center.y - textHeightCategory / 2
        )
        drawText(
            textLayoutResult = textLayoutResultCategory,
            topLeft = textOffsetCategory,
            color = Color.Black
        )
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

fun getCategorization(@FloatRange(from = 0.0, to = 45.0) currentBMI: Float): String {
    val result = when (currentBMI) {
        in 0.0..18.5 -> "Underweight"
        in 18.5..24.9 -> "Normal"
        in 24.9..29.9 -> "Overweight"
        in 29.9..34.9 -> "Obese"
        in 34.9..39.9 -> "Severely Obese"
        in 39.9..45.0 -> "Morbidly Obese"
        else -> "Unknown"
    }
    return result
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GaugePreview() {
    GaugeScreen(26.4f)
}
