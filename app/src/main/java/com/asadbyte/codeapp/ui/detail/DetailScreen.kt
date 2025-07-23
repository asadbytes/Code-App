package com.asadbyte.codeapp.ui.detail

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.data.ItemType
import com.asadbyte.codeapp.ui.generator.GeneratorViewModel
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    content: String,
    onNavigateBack: () -> Unit,
    generatorViewModel: GeneratorViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val bitmap by remember { mutableStateOf(generatorViewModel.generateQrCode(content)) }
    val isUrl = remember { Patterns.WEB_URL.matcher(content).matches() }

    var showShareDialog by remember { mutableStateOf(false) }

    if (showShareDialog) {
        ShareOptionDialog(
            onDismiss = { showShareDialog = false },
            onShareImage = {
                shareImage(context, bitmap!!)
                showShareDialog = false
            },
            onShareText = {
                shareText(context, content)
                showShareDialog = false
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "QR Code",
                    modifier = Modifier
                        .size(250.dp)
                        .padding(vertical = 16.dp)
                )
            }

            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Copy Button
                IconButton(onClick = {
                    clipboardManager.setText(AnnotatedString(content))
                }) {
                    Icon(painterResource(R.drawable.ic_content_copy), contentDescription = "Copy")
                }

                // Share Button
                IconButton(onClick = { showShareDialog = true /*shareContent(context, content)*/ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }

                // Open in Browser Button (only if it's a URL)
                if (isUrl) {
                    IconButton(onClick = { openUrl(context, content) }) {
                        Icon(painterResource(R.drawable.ic_open_in_browser), contentDescription = "Open in Browser")
                    }
                }
            }
        }
    }
}

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
        text = { Text("How would you like to share this item?") },
        confirmButton = {
            TextButton(onClick = onShareImage) {
                Text("QR Code (Image)")
            }
        },
        dismissButton = {
            TextButton(onClick = onShareText) {
                Text("Text")
            }
        }
    )
}

// Shares the content as plain text
private fun shareText(context: Context, content: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

// Shares the bitmap as an image
private fun shareImage(context: Context, bitmap: Bitmap) {
    // 1. Save bitmap to a file in the cache directory
    val cachePath = File(context.cacheDir, "images")
    cachePath.mkdirs() // Create the directory if it doesn't exist
    val file = File(cachePath, "qr_code.png")
    val fileOutputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
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