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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
fun EventInputScreen(
    onNavigateBack: () -> Unit,
    generatorViewModel: GeneratorViewModel = hiltViewModel(),
    onQrCodeGenerated: (Long) -> Unit
) {
    val uiState by generatorViewModel.uiState.collectAsState()
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
                modifier = Modifier
                    .size(120.dp)
                    .clickable { onNavigateBack() }
            )
            Text(
                text = "Event",
                color = Color.White,
                fontFamily = ItimFont,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
        //Spacer(modifier = Modifier.size(90.dp))
        EventInputCard(
            onGenerateClick = { text ->
                if (text.isNotBlank()) {
                    generatorViewModel.generateQrCode(text)
                }
            },
            modifier = Modifier
                .wrapContentSize()
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
fun EventInputCard(
    onGenerateClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var eventName by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }

    Card(
        modifier = modifier
            .padding(horizontal = 20.dp)
    ) {
        Box(
            modifier = Modifier
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
                    painter = painterResource(id = R.drawable.ic_input_event),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(top = 10.dp)
                        .align(Alignment.CenterHorizontally)
                )

                OurSpecialEventTextField(
                    title = "Event Name",
                    placeHolder = "Enter event name",
                    valueText = eventName,
                    onValueChange = { eventName = it }
                )
                OurSpecialEventTextField(
                    title = "Event Date and Time",
                    placeHolder = "12 Dec 2022, 10:40 pm",
                    valueText = eventDate,
                    onValueChange = { eventDate = it }
                )
                OurSpecialEventTextField(
                    title = "Event Location",
                    placeHolder = "Enter Location",
                    valueText = eventLocation,
                    onValueChange = { eventLocation = it }
                )
                OurSpecialEventTextField(
                    title = "Description",
                    placeHolder = "Enter any details",
                    valueText = eventDescription,
                    onValueChange = { eventDescription = it },
                    maxLines = 3,
                    modifier = Modifier.height(130.dp)
                )
                Spacer(modifier = Modifier.size(40.dp))
                Button(
                    onClick = {
                        val fields = EventFields(
                            eventName = eventName,
                            eventDate = eventDate,
                            eventLocation = eventLocation,
                            eventDescription = eventDescription
                        )
                        val formattedText = formatEventFields(fields)
                        onGenerateClick(formattedText)
                    },
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

@Composable
fun OurSpecialEventTextField(
    title: String,
    placeHolder: String,
    valueText: String,
    onValueChange: (String) -> Unit,
    maxLines: Int = 1,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        color = Color.White,
        fontFamily = ItimFont,
        modifier = Modifier.padding(start = 15.dp, top = 10.dp)
    )
    OutlinedTextField(
        value = valueText,
        maxLines = maxLines,
        placeholder = { Text(text = placeHolder) },
        onValueChange = { onValueChange(it) },
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
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
}


data class EventFields(
    val eventName: String,
    val eventDate: String,
    val eventLocation: String,
    val eventDescription: String
)

fun formatEventFields(fields: EventFields): String {
    return listOf(
        fields.eventName,
        fields.eventDate,
        fields.eventLocation,
        fields.eventDescription
    ).filter { it.isNotBlank() }
        .joinToString(separator = "\n")
}

//@Preview
//@Composable
//private fun EventInputPreview() {
//    CodeAppTheme {
//        EventInputScreen()
//    }
//}
