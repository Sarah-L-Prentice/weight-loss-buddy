package com.prenticeweb.weightlossbuddy.gauge

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prenticeweb.weightlossbuddy.R
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

class ProgressGauge


fun setContent(
    composeView: ComposeView,
    progressPercent: Float,
    weightLost: String,
    totalWeightToLose: String
) {
    composeView.setContent {
        GaugeProgressScreen(progressPercent, weightLost, totalWeightToLose)
    }
}


@Composable
fun GaugeProgressScreen(progressPercent: Float, weightLost: String, totalWeightToLose: String) {

    val textMeasurer = rememberTextMeasurer()
    val lightGrey = colorResource(R.color.lightGrey)
    val progressPct = progressPercent.absoluteValue

    Canvas(
        modifier = Modifier
            .padding(50.dp)
            .requiredSize(95.dp)
    ) {
        val angleSize = 270f
        val startAngle = 270 - angleSize / 2
        val sweepAngle = angleSize * progressPct
        val strokeWidth: Float = 20.0.dp.toPx()

        drawArc(
            color = lightGrey,
            startAngle = startAngle,
            sweepAngle = angleSize,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        drawArc(
            color = Color.Cyan,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )


        val textLayoutResultWeightLost = textMeasurer.measure(
            text = weightLost,
            style = TextStyle(
                fontSize = 20.sp
            )
        )

        val textHeight = textLayoutResultWeightLost.size.height
        val textWidth = textLayoutResultWeightLost.size.width
        val textOffset = pointOnCircle(
            thetaInDegrees = 0.0,
            radius = size.height / 15,
            cX = center.x - textWidth / 2,
            cY = center.y - textHeight / 2
        )
        drawText(
            textLayoutResult = textLayoutResultWeightLost,
            topLeft = textOffset,
            color = Color.Black
        )


    }
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
    GaugeProgressScreen(-0.5f, "3.00kg", "10.00kg")
}


