package com.asadbyte.codeapp.ui.scanner

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.data.HistoryItem
import com.asadbyte.codeapp.data.ItemType
import com.asadbyte.codeapp.ui.detail.DetailButton
import com.asadbyte.codeapp.ui.detail.ShareOptionDialog
import com.asadbyte.codeapp.ui.detail.shareImage
import com.asadbyte.codeapp.ui.detail.shareText
import com.asadbyte.codeapp.ui.generator.GeneratorViewModel
import com.asadbyte.codeapp.ui.history.HistoryViewModel
import com.asadbyte.codeapp.ui.others.enhanceQRCodeForSharing
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.Gray30
import com.asadbyte.codeapp.ui.theme.ItimFont
import com.asadbyte.codeapp.ui.theme.MyYellow

@Composable
fun NewResultScreen(
    generateId: Long?,
    onNavigateBack: () -> Unit
) {
    val historyViewModel: HistoryViewModel = hiltViewModel()
    val historyItems by historyViewModel.history.collectAsState()
    val item = historyItems.find { it.id == generateId }

    val generatorViewModel: GeneratorViewModel = hiltViewModel()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var showShareDialog by remember { mutableStateOf(false) }

    if (item != null) {
        val bitmap by remember { mutableStateOf(generatorViewModel.getQrCode(item!!.content)) }
        if (showShareDialog) {
            ShareOptionDialog(
                onDismiss = { showShareDialog = false },
                onShareImage = {
                    shareImage(context, bitmap!!)
                    showShareDialog = false
                },
                onShareText = {
                    shareText(context, item!!.content)
                    showShareDialog = false
                }
            )
        }

        // --- IMPROVEMENT: Added vertical scroll and horizontal alignment ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray10)
                .verticalScroll(rememberScrollState()), // Makes the entire screen scrollable
            horizontalAlignment = Alignment.CenterHorizontally // Centers children like card, image, buttons
        ) {
            // Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back_no_bg),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onNavigateBack() }
                )
                // --- IMPROVEMENT: Added space between icon and title ---
                Spacer(Modifier.width(16.dp))
                Text(
                    text = "Result",
                    fontFamily = ItimFont,
                    fontSize = 35.sp,
                    color = Color.White,
                )
            }

            ResultCard(item = item!!)

            // --- IMPROVEMENT: Added space above the QR code ---
            Spacer(Modifier.height(24.dp))

            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = "Generated QR Code",
                modifier = Modifier
                    .size(200.dp)
                    .border(4.dp, MyYellow)
            )

            // --- IMPROVEMENT: Added space above the action buttons ---
            Spacer(Modifier.height(24.dp))

            // Action Buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                // --- IMPROVEMENT: Used SpaceEvenly for better button distribution ---
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                DetailButton(
                    imageRes = R.drawable.ic_detail_share,
                    text = "Share",
                    onClick = { showShareDialog = true }
                )
                if (item!!.type == ItemType.SCAN) {
                    DetailButton(
                        imageRes = R.drawable.ic_detail_copy,
                        text = "Copy",
                        onClick = { clipboardManager.setText(AnnotatedString(item!!.content)) }
                    )
                } else {
                    DetailButton(
                        imageRes = R.drawable.ic_detail_save,
                        text = "Save",
                        onClick = {
                            bitmap?.let {
                                saveBitmapToGallery(context, it, title = "QRCode_${item!!.id}")
                            }
                        }
                    )
                }
            }
        }
    } else {
        // Fallback case for when item is not found
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Using a CircularProgressIndicator while the item is loading (initial state is null)
            CircularProgressIndicator()
        }
    }
}


@Composable
fun ResultCard(
    item: HistoryItem
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = Modifier
            .fillMaxWidth()
            // --- IMPROVEMENT: Adjusted padding for better consistency ---
            .padding(vertical = 16.dp, horizontal = 24.dp)
            .shadow(
                elevation = 30.dp,
                spotColor = Color.Black,
                ambientColor = Color.White
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray30)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = item.type.name,
                color = Color.White,
            )

            // --- IMPROVEMENT: Made the text area scrollable as requested ---
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .height(72.dp) // Sets a fixed height (~3 lines)
                    .fillMaxWidth()
                    .verticalScroll(scrollState) // Makes the content scroll if it overflows
            ) {
                Text(
                    text = item.content,
                    color = Color.White,
                    // maxLines and overflow are removed to allow for scrolling
                )
            }
        }
    }
}

// Re-using the improved DetailButton from the previous screen
@Composable
fun DetailButton(
    imageRes: Int,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp), // Increased space
        modifier = Modifier
            .clickable { onClick() }
            .padding(16.dp) // Added padding for a better touch area
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = text // Using text as content description for accessibility
        )
        Text(
            text = text,
            color = Color.White,
        )
    }
}

fun saveBitmapToGallery(context: Context, bitmap: Bitmap, title: String = "QRCode_${System.currentTimeMillis()}") {

    val enhancedBitmap = enhanceQRCodeForSharing(bitmap)

    val filename = "$title.png"
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/QRCodeApp")
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val contentResolver = context.contentResolver
    val imageUri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    if (imageUri != null) {
        contentResolver.openOutputStream(imageUri).use { outputStream ->
            enhancedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream!!)
        }

        // Mark as not pending to make it visible in Gallery apps
        contentValues.clear()
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
        contentResolver.update(imageUri, contentValues, null, null)

        Toast.makeText(context, "Saved to Gallery", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
    }
}

@Preview
@Composable
private fun ResultPreview() {
    CodeAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray10)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back_no_bg),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {  }
                )
                Text(
                    text = "Result",
                    fontFamily = ItimFont,
                    fontSize = 35.sp,
                    color = Color.White
                )
            }
            ResultCard(item = HistoryItem(
                id = 1,
                content = "Hello World",
                type = ItemType.SCAN,
                timestamp = System.currentTimeMillis(),
                isFavorite = false
            )
            )
            Image(
                painter = painterResource(R.drawable.ic_start_screen_qrcode),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(4.dp, MyYellow)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                DetailButton(
                    imageRes = R.drawable.ic_detail_share,
                    text = "Share",
                    onClick = {  }
                )
                if(true) {
                    DetailButton(
                        imageRes = R.drawable.ic_detail_copy,
                        text = "Copy",
                        onClick = { }
                    )
                } else {
                    DetailButton(
                        imageRes = R.drawable.ic_detail_save,
                        text = "Save",
                        onClick = {  }
                    )
                }
            }
        }
    }
}