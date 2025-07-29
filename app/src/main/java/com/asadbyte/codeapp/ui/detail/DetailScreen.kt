package com.asadbyte.codeapp.ui.detail

import android.util.Patterns
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.data.HistoryItem
import com.asadbyte.codeapp.data.ItemType
import com.asadbyte.codeapp.ui.generator.GeneratorViewModel
import com.asadbyte.codeapp.ui.history.formatTimestamp
import com.asadbyte.codeapp.ui.scanner.saveBitmapToGallery
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.Gray30
import com.asadbyte.codeapp.ui.theme.ItimFont
import com.asadbyte.codeapp.ui.theme.MyYellow

@Composable
fun DetailScreen(
    item: HistoryItem?,
    onNavigateBack: () -> Unit = {},
    generatorViewModel: GeneratorViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var showShareDialog by remember { mutableStateOf(false) }

    if (item != null) {
        val bitmap by remember { mutableStateOf(generatorViewModel.getQrCode(item.content)) }
        // This check is removed as it's not used in the provided UI logic
        // val isUrl = remember { Patterns.WEB_URL.matcher(item.content).matches() }

        if (showShareDialog) {
            ShareOptionDialog(
                onDismiss = { showShareDialog = false },
                onShareImage = {
                    shareImage(context, bitmap!!)
                    showShareDialog = false
                },
                onShareText = {
                    shareText(context, item.content)
                    showShareDialog = false
                }
            )
        }

        // --- IMPROVEMENT: Made the Column scrollable and centered its children ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray10)
                .verticalScroll(rememberScrollState()), // Makes the content scrollable
            horizontalAlignment = Alignment.CenterHorizontally
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
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Details",
                    fontFamily = ItimFont,
                    fontSize = 35.sp,
                    color = Color.White
                )
            }

            // Detail Card
            DetailCard(
                item = item,
                imageBitmap = bitmap!!.asImageBitmap()
            )

            // Action Buttons
            // --- IMPROVEMENT: Used SpaceEvenly for better distribution ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly, // Distributes buttons evenly
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp) // Adds vertical space around the buttons
            ) {
                DetailButton(
                    imageRes = R.drawable.ic_detail_share,
                    text = "Share",
                    onClick = { showShareDialog = true }
                )
                if (item.type == ItemType.SCAN) {
                    DetailButton(
                        imageRes = R.drawable.ic_detail_copy,
                        text = "Copy",
                        onClick = { clipboardManager.setText(AnnotatedString(item.content)) }
                    )
                } else {
                    DetailButton(
                        imageRes = R.drawable.ic_detail_save,
                        text = "Save",
                        onClick = {
                            bitmap?.let {
                                saveBitmapToGallery(context, it, title = "QRCode_${item.id}")
                            }
                        }
                    )
                }
            }
        }
    } else {
        // Fallback case remains the same
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Got no item to display",
                color = Color.Red,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun DetailButton(
    imageRes: Int,
    text: String,
    onClick: () -> Unit
) {
    // --- IMPROVEMENT: Increased spacing and added padding for a larger touch target ---
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp), // Increased space between icon and text
        modifier = Modifier
            .clickable { onClick() }
            .padding(16.dp) // Added padding for a better touch area and visual spacing
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = text // Using text as content description
        )
        Text(
            text = text,
            color = Color.White,
        )
    }
}

@Composable
fun DetailCard(
    item: HistoryItem,
    imageBitmap: ImageBitmap
) {
    var showQRCode by remember { mutableStateOf(false) }

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
                .background(Gray30),
            // --- IMPROVEMENT: Center-align children of this column ---
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp) // Standardized padding
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_bottom_bar_scanner),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.width(16.dp)) // Added spacer
                Column {
                    Text(
                        text = item.type.name,
                        color = Color.White
                    )
                    Text(
                        text = formatTimestamp(item.timestamp),
                        color = Color.White
                    )
                }
            }
            HorizontalDivider(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp) // Adjusted padding
            )
            Text(
                text = item.content,
                color = Color.White,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp) // Adjusted padding
            )
            if (showQRCode) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "QR Code for ${item.content}", // Added accessibility
                    modifier = Modifier
                        // --- IMPROVEMENT: Added padding to space the QR code out ---
                        .padding(vertical = 16.dp)
                        .size(200.dp)
                        .border(4.dp, MyYellow)
                )
            }
            TextButton(
                onClick = { showQRCode = !showQRCode },
                colors = ButtonDefaults.textButtonColors(contentColor = MyYellow), // Simplified API
                modifier = Modifier.padding(bottom = 8.dp) // Ensure space at the bottom
            ) {
                Text(
                    text = if (showQRCode) "Hide QR Code" else "Show QR Code",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun DetailNewPreview() {
//    CodeAppTheme {
//        DetailScreenNew()
//    }
//}