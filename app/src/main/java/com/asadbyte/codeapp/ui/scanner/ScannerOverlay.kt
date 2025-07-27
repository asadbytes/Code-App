package com.asadbyte.codeapp.ui.scanner

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.asadbyte.codeapp.ui.theme.MyYellow

/**
 * A composable that draws a scanner overlay with a transparent central box,
 * a thin border, and bold corners.
 */
@Composable
fun ScannerOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val boxSize = size.width * 0.7f
        val boxTopLeft = Offset(
            x = (size.width - boxSize) / 2,
            y = (size.height - boxSize) / 2
        )
        val boxRect = Rect(offset = boxTopLeft, size = Size(boxSize, boxSize))

        // 1. Draw the semi-transparent background scrim
        drawRect(
            color = Color.Black.copy(alpha = 0.5f),
        )

        // 2. Punch a hole in the center
        drawRect(
            topLeft = boxRect.topLeft,
            size = boxRect.size,
            color = Color.Transparent,
            blendMode = BlendMode.Clear // This is what makes the center transparent
        )

        // 3. Draw the thin gray border around the cutout
        drawRect(
            topLeft = boxRect.topLeft,
            size = boxRect.size,
            color = MyYellow/*Color.Gray*/,
            style = Stroke(width = 2.dp.toPx())
        )

        // 4. Draw the bold corners
        val cornerLength = 30.dp.toPx()
        val cornerStrokeWidth = 8.dp.toPx()
        val cornerColor = Color(0xFF0D1B2A) // A dark, bold color for corners

        // Top-left corner path
        val topLeftPath = Path().apply {
            moveTo(boxRect.left, boxRect.top + cornerLength)
            lineTo(boxRect.left, boxRect.top)
            lineTo(boxRect.left + cornerLength, boxRect.top)
        }
        drawPath(
            path = topLeftPath,
            color = cornerColor,
            style = Stroke(width = cornerStrokeWidth, cap = StrokeCap.Butt)
        )

        // Top-right corner path
        val topRightPath = Path().apply {
            moveTo(boxRect.right - cornerLength, boxRect.top)
            lineTo(boxRect.right, boxRect.top)
            lineTo(boxRect.right, boxRect.top + cornerLength)
        }
        drawPath(
            path = topRightPath,
            color = cornerColor,
            style = Stroke(width = cornerStrokeWidth, cap = StrokeCap.Butt)
        )

        // Bottom-left corner path
        val bottomLeftPath = Path().apply {
            moveTo(boxRect.left, boxRect.bottom - cornerLength)
            lineTo(boxRect.left, boxRect.bottom)
            lineTo(boxRect.left + cornerLength, boxRect.bottom)
        }
        drawPath(
            path = bottomLeftPath,
            color = cornerColor,
            style = Stroke(width = cornerStrokeWidth, cap = StrokeCap.Butt)
        )

        // Bottom-right corner path
        val bottomRightPath = Path().apply {
            moveTo(boxRect.right, boxRect.bottom - cornerLength)
            lineTo(boxRect.right, boxRect.bottom)
            lineTo(boxRect.right - cornerLength, boxRect.bottom)
        }
        drawPath(
            path = bottomRightPath,
            color = cornerColor,
            style = Stroke(width = cornerStrokeWidth, cap = StrokeCap.Butt)
        )
    }
}