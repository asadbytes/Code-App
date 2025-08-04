package com.asadbyte.codeapp.ui.generator.input

import android.inputmethodservice.Keyboard
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asadbyte.codeapp.MainActivity
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.ui.adsMob.AdViewModel
import com.asadbyte.codeapp.ui.generator.GeneratorViewModel
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.Gray20
import com.asadbyte.codeapp.ui.theme.Gray30
import com.asadbyte.codeapp.ui.theme.ItimFont
import com.asadbyte.codeapp.ui.theme.MyYellow

@Composable
fun WifiInputScreen(
    onNavigateBack: () -> Unit,
    generatorViewModel: GeneratorViewModel = hiltViewModel(),
    adViewModel: AdViewModel,
    onQrCodeGenerated: (Long) -> Unit
) {
    val uiState by generatorViewModel.uiState.collectAsState()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray10)
            .statusBarsPadding() // Handles spacing for the system status bar
            .imePadding()       // Handles spacing for the keyboard
    ) {
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_no_bg),
                contentDescription = "Back",
                modifier = Modifier
                    .size(32.dp) // Resized from 120.dp to a standard size
                    .clickable { onNavigateBack() }
            )
            Spacer(Modifier.width(12.dp)) // Added space for balance
            Text(
                text = "Wi-Fi",
                color = Color.White,
                fontFamily = ItimFont,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        Spacer(modifier = Modifier.height(80.dp))
        // WifiInputCard now uses weight to flexibly fill the remaining space
        WifiInputCard(
            onGenerateClick = { text ->
                if (text.isNotBlank()) {
                    generatorViewModel.generateQrCode(text)
                }
            },
            modifier = Modifier.weight(1f)
        )
    }

    // Your LaunchedEffect logic is untouched
    LaunchedEffect(uiState.generatedId) {
        if (uiState.capturedBitmap != null) {
            adViewModel.showInterstitialAd(
                activity = context as MainActivity
            )
            onQrCodeGenerated(uiState.generatedId!!)
            generatorViewModel.clearGeneratedQrCode()
        }
    }
}

@Composable
fun WifiInputCard(
    onGenerateClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    // Your state logic is untouched
    var networkName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val visibilityOnIcon = painterResource(R.drawable.ic_visibility_on)
    val visibilityOffIcon = painterResource(R.drawable.ic_visibility_off)

    // This outer Column handles scrolling for all the content inside
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            // Your Box with the drawBehind border is preserved
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
                        // Bottom border
                        drawLine(
                            color = color,
                            start = Offset(0f, size.height - strokeWidth / 2),
                            end = Offset(size.width, size.height - strokeWidth / 2),
                            strokeWidth = strokeWidth
                        )
                    }
            ) {
                // This inner Column now just holds content and doesn't scroll itself
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_input_wifi),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.size(10.dp))

                    // The alignment of these children is now controlled by the parent Column
                    Column(horizontalAlignment = Alignment.Start) {

                        OurSpecialEventTextField(
                            title = "Network",
                            placeHolder = "Enter Network Name",
                            valueText = networkName,
                            onValueChange = { networkName = it },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )
                        Text(
                            text = "Password",
                            color = Color.White,
                            fontFamily = ItimFont,
                            modifier = Modifier.padding(start = 15.dp, top = 10.dp)
                        )
                        OutlinedTextField(
                            value = password,
                            maxLines = 1,
                            placeholder = { Text(text = "Enter password") },
                            onValueChange = { password = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            shape = RoundedCornerShape(12.dp),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(
                                    onClick = { passwordVisible = !passwordVisible },
                                    modifier = Modifier.size(29.dp)
                                ) {
                                    Icon(
                                        painter = if (passwordVisible) visibilityOnIcon else visibilityOffIcon,
                                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                        tint = MyYellow
                                    )
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Gray10,
                                focusedContainerColor = Gray30,
                                unfocusedContainerColor = Gray30,
                                cursorColor = MyYellow,
                                focusedPlaceholderColor = Gray10,
                                unfocusedPlaceholderColor = Gray10,
                                focusedIndicatorColor = MyYellow,
                                unfocusedIndicatorColor = Gray10
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            )
                        )
                    }

                    Spacer(modifier = Modifier.size(40.dp))

                    Button(
                        onClick = {
                            val formatted = formatWifiFields(networkName, password)
                            onGenerateClick(formatted)
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
                }
            }
        }
        // Spacer at the bottom for better padding when fully scrolled down
        Spacer(Modifier.height(16.dp))
    }
}

fun formatWifiFields(networkName: String, password: String): String {
    return listOf(networkName, password)
        .filter { it.isNotBlank() }
        .joinToString(separator = "\n")
}

@Preview
@Composable
private fun GenerateInputPreview() {
    CodeAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray10)
                .statusBarsPadding() // Handles spacing for the system status bar
                .imePadding()       // Handles spacing for the keyboard
        ) {
            // Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back_no_bg),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(32.dp) // Resized from 120.dp to a standard size
                        .clickable { }
                )
                Spacer(Modifier.width(12.dp)) // Added space for balance
                Text(
                    text = "Wi-Fi",
                    color = Color.White,
                    fontFamily = ItimFont,
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
            // WifiInputCard now uses weight to flexibly fill the remaining space
            WifiInputCard(
                onGenerateClick = { },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
