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
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.Gray20
import com.asadbyte.codeapp.ui.theme.Gray30
import com.asadbyte.codeapp.ui.theme.ItimFont
import com.asadbyte.codeapp.ui.theme.MyYellow

@Composable
fun ContactInputScreen(modifier: Modifier = Modifier) {
    var networkName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray10)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_detail_back),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clickable { }
            )
            Text(
                text = "Contact",
                color = Color.White,
                fontFamily = ItimFont,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
        ContactInputCard(
            networkName = networkName,
            password = password,
            onNetworkChange = { networkName = it},
            onPasswordChange = { password = it},
            onGenerateClick = { },
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun ContactInputCard(
    networkName: String,
    password: String,
    onNetworkChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val visibilityOnIcon = painterResource(R.drawable.ic_visibility_on)
    val visibilityOffIcon = painterResource(R.drawable.ic_visibility_off)
    Card(
        modifier = modifier
            .padding(horizontal = 10.dp)
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
                modifier = Modifier.padding(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_input_contact),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(top = 10.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OurSpecialContactTextField(
                        title = "First Name",
                        placeHolder = "Enter name",
                        valueText = networkName,
                        onValueChange = { onNetworkChange(it) },
                        modifier = Modifier.weight(1f)
                    )
                    OurSpecialContactTextField(
                        title = "Last Name",
                        placeHolder = "Enter Name",
                        valueText = password,
                        onValueChange = { onPasswordChange(it) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OurSpecialContactTextField(
                        title = "Company",
                        placeHolder = "Enter comp...",
                        valueText = networkName,
                        onValueChange = { onNetworkChange(it) },
                        modifier = Modifier.weight(1f)
                    )
                    OurSpecialContactTextField(
                        title = "Job",
                        placeHolder = "Enter job",
                        valueText = password,
                        onValueChange = { onPasswordChange(it) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OurSpecialContactTextField(
                        title = "Phone",
                        placeHolder = "Enter phone",
                        valueText = networkName,
                        onValueChange = { onNetworkChange(it) },
                        modifier = Modifier.weight(1f)
                    )
                    OurSpecialContactTextField(
                        title = "Email",
                        placeHolder = "Enter email",
                        valueText = password,
                        onValueChange = { onPasswordChange(it) },
                        modifier = Modifier.weight(1f)
                    )
                }

                OurSpecialContactTextField(
                    title = "Address",
                    placeHolder = "Enter address",
                    valueText = password,
                    onValueChange = { onPasswordChange(it) },
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OurSpecialContactTextField(
                        title = "City",
                        placeHolder = "Enter city",
                        valueText = networkName,
                        onValueChange = { onNetworkChange(it) },
                        modifier = Modifier.weight(1f)
                    )
                    OurSpecialContactTextField(
                        title = "Country",
                        placeHolder = "Enter country",
                        valueText = password,
                        onValueChange = { onPasswordChange(it) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.size(20.dp))
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
                Spacer(modifier = Modifier.size(10.dp))
            }
        }
    }
}

@Composable
fun OurSpecialContactTextField(
    title: String,
    placeHolder: String,
    valueText: String,
    onValueChange: (String) -> Unit,
    maxLines: Int = 1,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
            modifier = Modifier
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
}

@Preview
@Composable
private fun EventInputPreview() {
    CodeAppTheme {
        ContactInputScreen()
    }
}
