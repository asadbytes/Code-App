package com.asadbyte.codeapp.ui.generator.input

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.ui.generator.GeneratorViewModel
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.Gray20
import com.asadbyte.codeapp.ui.theme.Gray30
import com.asadbyte.codeapp.ui.theme.ItimFont
import com.asadbyte.codeapp.ui.theme.MyYellow

@Composable
fun GenerateInputHandler(
    cardData: SimpleInputCardData,
    onNavigateBack: () -> Unit,
    generatorViewModel: GeneratorViewModel = hiltViewModel(),
    onQrCodeGenerated: (Long) -> Unit
) {
    val text by generatorViewModel.inputText.collectAsState()
    val uiState by generatorViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(Gray10)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_detail_back),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clickable { onNavigateBack() }
            )
            Text(
                text = cardData.title,
                color = Color.White,
                fontFamily = ItimFont,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
        Spacer(modifier = Modifier.size(120.dp))
        SimpleInputCard(
            textValue = text,
            onValueChange = { generatorViewModel.onInputTextChanged(it) },
            cardData = cardData,
            onGenerateClick = {
                if (text.isNotBlank()) {
                    generatorViewModel.generateQrCode(text)
                    //navigationPending = true
                }
            },
            modifier = Modifier
                .height(336.5.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
    }
    LaunchedEffect(uiState.generatedId) {
        if (uiState.capturedBitmap != null) {
            onQrCodeGenerated(uiState.generatedId!!)
            generatorViewModel.clearGeneratedQrCode()
        }
    }
}

@Composable
fun SimpleInputCard(
    textValue: String,
    onValueChange: (String) -> Unit,
    cardData: SimpleInputCardData,
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(horizontal = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray20)
                .drawBehind {
                    val strokeWidth = 3.dp.toPx()
                    val color = MyYellow
                    // Top border
                    drawLine(
                        color = color,
                        start = Offset(0f, strokeWidth / 2),
                        end = Offset(size.width, strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                    // Bottom border - use actual size.height
                    drawLine(
                        color = color,
                        start = Offset(0f, size.height - strokeWidth / 2),
                        end = Offset(size.width, size.height - strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = cardData.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(top = 10.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = cardData.title,
                    color = Color.White,
                    fontFamily = ItimFont,
                    modifier = Modifier.padding(start = 15.dp, top = 10.dp)
                )
                OutlinedTextField(
                    value = textValue,
                    maxLines = 1,
                    placeholder = { Text(text = cardData.placeholder) },
                    onValueChange = { onValueChange(it) },
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Gray10,
                        unfocusedContainerColor = Gray30,
                        cursorColor = MyYellow,
                        focusedPlaceholderColor = Gray10,
                        unfocusedPlaceholderColor = Gray10,
                        focusedIndicatorColor = MyYellow,
                        unfocusedIndicatorColor = Gray10
                    )
                )
                Spacer(modifier = Modifier.size(40.dp))
                Button(
                    onClick = { onGenerateClick() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MyYellow,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Generate QR Code",
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
                Spacer(modifier = Modifier.size(20.dp))
            }
        }
    }
}

data class SimpleInputCardData(
    val imageRes: Int,
    val title: String,
    val placeholder: String
)

val inputCardData = mapOf(
    "text" to SimpleInputCardData(
        imageRes = R.drawable.ic_input_text,
        title = "Text",
        placeholder = "Enter text"
    ),
    "website" to SimpleInputCardData(
        imageRes = R.drawable.ic_input_website,
        title = "Website URL",
        placeholder = "www.qrcode.com"
    ),
    "location" to SimpleInputCardData(
        imageRes = R.drawable.ic_input_location,
        title = "Location",
        placeholder = "Enter location"
    ),
    "whatsapp" to SimpleInputCardData(
        imageRes = R.drawable.ic_input_whatsapp,
        title = "WhatsApp Link",
        placeholder = "Enter whatsapp link"
    ),
    "twitter" to SimpleInputCardData(
        imageRes = R.drawable.ic_input_twitter,
        title = "Profile Link",
        placeholder = "Enter twitter link"
    ),
    "email" to SimpleInputCardData(
        imageRes = R.drawable.ic_input_email,
        title = "Email",
        placeholder = "Enter email address"
    ),
    "instagram" to SimpleInputCardData(
        imageRes = R.drawable.ic_input_instagram,
        title = "Profile Link",
        placeholder = "Enter instagram link"
    ),
    "phone" to SimpleInputCardData(
        imageRes = R.drawable.ic_input_phone,
        title = "Phone Number",
        placeholder = "+92xxxxxxxxx"
    ),
)