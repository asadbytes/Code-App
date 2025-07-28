package com.asadbyte.codeapp.ui.detail

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.asadbyte.codeapp.ui.others.enhanceQRCodeForSharing
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.MyYellow
import java.io.File
import java.io.FileOutputStream

private fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

@Composable
fun ShareOptionDialog(
    onDismiss: () -> Unit,
    onShareImage: () -> Unit,
    onShareText: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Share As") },
        text = { Text("How would you like to share this item?", color = Color.White) },
        confirmButton = {
            TextButton(onClick = onShareImage) {
                Text("QR Code (Image)", color = MyYellow)
            }
        },
        dismissButton = {
            TextButton(onClick = onShareText) {
                Text("Text", color = MyYellow)
            }
        },
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(BorderStroke(2.dp, MyYellow), shape = RoundedCornerShape(16.dp))
            .background(Gray10)
    )
}

// Shares the content as plain text
fun shareText(context: Context, content: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

// Shares the bitmap as an image
fun shareImage(context: Context, bitmap: Bitmap) {

    val enhancedBitmap = enhanceQRCodeForSharing(bitmap)

    // 1. Save bitmap to a file in the cache directory
    val cachePath = File(context.cacheDir, "images")
    cachePath.mkdirs() // Create the directory if it doesn't exist
    val file = File(cachePath, "qr_code.png")
    val fileOutputStream = FileOutputStream(file)
    enhancedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
    fileOutputStream.close()

    // 2. Get a content URI using FileProvider
    val imageUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider", // Ensure this matches your manifest
        file
    )

    // 3. Create the share intent
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, imageUri)
        type = "image/png"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share QR Code"))
}