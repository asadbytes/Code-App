package com.asadbyte.codeapp.ui.others

import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.Typeface
import androidx.compose.ui.graphics.toArgb
import com.asadbyte.codeapp.ui.theme.MyYellow

fun enhanceQRCodeForSharing(originalBitmap: Bitmap): Bitmap {
    // Constants for styling
    val qrBorderWidth = 10f
    val overallBorderWidth = 20f
    val titleHeight = 120f
    val padding = 60f

    // Calculate dimensions
    val qrWithBorderSize = originalBitmap.width + (qrBorderWidth * 2).toInt()
    val totalWidth = qrWithBorderSize + (overallBorderWidth * 2).toInt()
    val totalHeight =
        qrWithBorderSize + titleHeight.toInt() + (overallBorderWidth * 2).toInt() + padding.toInt()

    // Create the final bitmap
    val enhancedBitmap =
        Bitmap.createBitmap(totalWidth, totalHeight.toInt(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(enhancedBitmap)

    // Create paint objects
    val qrBorderPaint = Paint().apply {
        color = MyYellow.toArgb()
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    val overallBorderPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    val shadowPaint = Paint().apply {
        color = Color.parseColor("#40000000") // Semi-transparent black
        maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
        isAntiAlias = true
    }

    val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 38f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    // Fill the entire canvas with white background
    canvas.drawColor(Color.WHITE)

    // Draw subtle shadow for depth
    val shadowOffset = 6f
    canvas.drawRoundRect(
        overallBorderWidth + shadowOffset,
        overallBorderWidth + shadowOffset,
        totalWidth - overallBorderWidth + shadowOffset,
        totalHeight - overallBorderWidth + shadowOffset,
        16f, 16f, shadowPaint
    )

    // Draw overall border with rounded corners
    canvas.drawRoundRect(
        overallBorderWidth,
        overallBorderWidth,
        totalWidth - overallBorderWidth,
        totalHeight - overallBorderWidth,
        16f, 16f, overallBorderPaint
    )

    // Draw a subtle gradient background
    val gradientPaint = Paint().apply {
        shader = LinearGradient(
            0f, 0f, 0f, totalHeight.toFloat(),
            Color.parseColor("#FAFAFA"),
            Color.parseColor("#F0F0F0"),
            Shader.TileMode.CLAMP
        )
    }
    canvas.drawRoundRect(
        overallBorderWidth,
        overallBorderWidth,
        totalWidth - overallBorderWidth,
        totalHeight - overallBorderWidth,
        16f, 16f, gradientPaint
    )

    // Calculate positions for QR code
    val qrStartX =
        overallBorderWidth + ((totalWidth - overallBorderWidth * 2 - qrWithBorderSize) / 2)
    val qrStartY = overallBorderWidth + padding / 2

    // Draw QR code border (MyYellow)
    canvas.drawRoundRect(
        qrStartX.toFloat(),
        qrStartY,
        (qrStartX + qrWithBorderSize).toFloat(),
        qrStartY + qrWithBorderSize,
        12f, 12f, qrBorderPaint
    )

    // Draw the original QR code bitmap
    val qrDestRect = Rect(
        (qrStartX + qrBorderWidth).toInt(),
        (qrStartY + qrBorderWidth).toInt(),
        (qrStartX + qrBorderWidth + originalBitmap.width).toInt(),
        (qrStartY + qrBorderWidth + originalBitmap.height).toInt()
    )
    canvas.drawBitmap(originalBitmap, null, qrDestRect, null)

    return enhancedBitmap
}