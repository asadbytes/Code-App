package com.asadbyte.codeapp.ui.detail

import android.util.Log
import android.util.Patterns
import android.widget.ImageButton
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.asadbyte.codeapp.ui.generator.GeneratorViewModel
import com.asadbyte.codeapp.ui.history.HistoryViewModel
import com.asadbyte.codeapp.ui.history.formatTimestamp
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.Gray30
import com.asadbyte.codeapp.ui.theme.ItimFont
import com.asadbyte.codeapp.ui.theme.MyYellow

@Composable
fun DetailScreenNew(
    item: HistoryItem?,
    onNavigateBack: () -> Unit = {},
    generatorViewModel: GeneratorViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var showShareDialog by remember { mutableStateOf(false) }

    if(item != null) {
        val bitmap by remember { mutableStateOf(generatorViewModel.getQrCode(item.content)) }
        val isUrl = remember { Patterns.WEB_URL.matcher(item.content).matches() }
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
            DetailCard(
                item = item,
                imageBitmap = bitmap!!.asImageBitmap()
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
                DetailButton(
                    imageRes = R.drawable.ic_detail_copy,
                    text = "Copy",
                    onClick = { clipboardManager.setText(AnnotatedString(item!!.content)) }
                )
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
fun DetailButton(
    imageRes: Int,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.padding(0.dp)
        )
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier.padding(0.dp)
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_bottom_bar_scanner),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
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
                    .padding(horizontal = 30.dp)
            )
            Text(
                text = item.content,
                color = Color.White,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp)
            )
            if (showQRCode) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.CenterHorizontally)
                        .border(4.dp, MyYellow)
                )
            }
            TextButton(
                onClick = { showQRCode = !showQRCode },
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MyYellow,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = MyYellow
                ),
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                Text(
                    text = if(showQRCode) "Hide QR Code" else "Show QR Code",
                    modifier = Modifier.fillMaxWidth(),
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