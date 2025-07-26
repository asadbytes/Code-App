package com.asadbyte.codeapp.ui.others

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.data.HistoryItem
import com.asadbyte.codeapp.data.ItemType
import com.asadbyte.codeapp.ui.detail.DetailButton
import com.asadbyte.codeapp.ui.detail.DetailCard
import com.asadbyte.codeapp.ui.detail.ShareOptionDialog
import com.asadbyte.codeapp.ui.detail.shareImage
import com.asadbyte.codeapp.ui.detail.shareText
import com.asadbyte.codeapp.ui.generator.GeneratorViewModel
import com.asadbyte.codeapp.ui.history.formatTimestamp
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.Gray30
import com.asadbyte.codeapp.ui.theme.ItimFont
import com.asadbyte.codeapp.ui.theme.MyYellow

@Composable
fun NewResultScreen(
    item: HistoryItem?,
    onNavigateBack: () -> Unit = {}
) {
    val generatorViewModel: GeneratorViewModel = hiltViewModel()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var showShareDialog by remember { mutableStateOf(false) }
    if(item != null) {
        val bitmap by remember { mutableStateOf(generatorViewModel.getQrCode(item.content)) }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray10)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_detail_back),
                    contentDescription = null,
                    modifier = Modifier.clickable { onNavigateBack() }
                )
                Text(
                    text = "Result",
                    fontFamily = ItimFont,
                    fontSize = 35.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 10.dp)

                )
            }
            ResultCard(item = item)
            Image(
                bitmap = bitmap!!.asImageBitmap(),
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
                    onClick = { showShareDialog = true }
                )
                if(item.type == ItemType.SCAN) {
                    DetailButton(
                        imageRes = R.drawable.ic_detail_copy,
                        text = "Copy",
                        onClick = { clipboardManager.setText(AnnotatedString(item.content)) }
                    )
                } else {
                    DetailButton(
                        imageRes = R.drawable.ic_detail_save,
                        text = "Save",
                        onClick = { bitmap?.let { saveBitmapToGallery(context, it, title = "QRCode_${item.id}") } }
                    )
                }
            }
        }
    } else {
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
fun ResultCard(
    item: HistoryItem
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 30.dp)
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
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text(
                    text = item.type.name,
                    color = Color.White,
                )
                Text(
                    text = item.content,
                    color = Color.White,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

fun saveBitmapToGallery(context: Context, bitmap: Bitmap, title: String = "QRCode_${System.currentTimeMillis()}") {
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
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream!!)
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
