package com.asadbyte.codeapp.ui.generator.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.ui.generator.GeneratorViewModel
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.Gray20
import com.asadbyte.codeapp.ui.theme.ItimFont
import com.asadbyte.codeapp.ui.theme.MyYellow
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset

// Data class to hold the state for the input fields
data class BusinessDetailsState(
    val companyName: String = "",
    val industry: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val city: String = "",
    val country: String = ""
)

@Composable
fun BusinessInputScreen(
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
        // Add a spacer to push content down from the system status bar.
        Spacer(Modifier.height(16.dp))

        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                // Add vertical padding for top and bottom spacing.
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_no_bg),
                contentDescription = "Back",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onNavigateBack() }
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Business",
                color = Color.White,
                fontFamily = ItimFont,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        // Card with input fields
        BusinessInputCard(
            onGenerateClick = { details ->
                val businessFields = BusinessFields(
                    companyName = details.companyName,
                    industry = details.industry,
                    phone = details.phone,
                    email = details.email,
                    address = details.address,
                    city = details.city,
                    country = details.country
                )
                val formattedDetails = formatBusinessFields(businessFields)
                generatorViewModel.generateQrCode(formattedDetails)
            },
            modifier = Modifier
                .fillMaxWidth()
                // Use weight to fill remaining space instead of fillMaxSize
                .weight(1f)
                .padding(16.dp)
        )
    }

    // LaunchedEffect remains the same
    LaunchedEffect(uiState.generatedId) {
        if (uiState.generatedId != null) {
            onQrCodeGenerated(uiState.generatedId!!)
            generatorViewModel.clearGeneratedQrCode()
        }
    }
}

@Composable
fun BusinessInputCard(
    onGenerateClick: (BusinessDetailsState) -> Unit,
    modifier: Modifier = Modifier
) {
    // State holder remains the same
    var details by remember { mutableStateOf(BusinessDetailsState()) }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card {
            // Box to contain the background and custom border drawing
            Box(
                modifier = Modifier
                    .background(Gray20)
                    .drawBehind {
                        val strokeWidth = 2.dp.toPx() // Using 2.dp to match previous BorderStroke
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
                // The rest of the layout remains the same
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_input_business),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(Modifier.height(24.dp))

                    OurSpecialContactTextField(
                        title = "Company Name",
                        placeHolder = "Enter name",
                        valueText = details.companyName,
                        onValueChange = { details = details.copy(companyName = it) }
                    )

                    OurSpecialContactTextField(
                        title = "Industry",
                        placeHolder = "e.g Food/Agency",
                        valueText = details.industry,
                        onValueChange = { details = details.copy(industry = it) }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OurSpecialContactTextField(
                            title = "Phone",
                            placeHolder = "Enter phone",
                            valueText = details.phone,
                            onValueChange = { details = details.copy(phone = it) },
                            modifier = Modifier.weight(1f)
                        )
                        OurSpecialContactTextField(
                            title = "Email",
                            placeHolder = "Enter email",
                            valueText = details.email,
                            onValueChange = { details = details.copy(email = it) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OurSpecialContactTextField(
                        title = "Address",
                        placeHolder = "Enter address",
                        valueText = details.address,
                        onValueChange = { details = details.copy(address = it) },
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OurSpecialContactTextField(
                            title = "City",
                            placeHolder = "Enter city",
                            valueText = details.city,
                            onValueChange = { details = details.copy(city = it) },
                            modifier = Modifier.weight(1f)
                        )
                        OurSpecialContactTextField(
                            title = "Country",
                            placeHolder = "Enter country",
                            valueText = details.country,
                            onValueChange = { details = details.copy(country = it) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(32.dp))

                    Button(
                        onClick = { onGenerateClick(details) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MyYellow,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Generate QR Code",
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

data class BusinessFields(
    val companyName: String,
    val industry: String,
    val phone: String,
    val email: String,
    val address: String,
    val city: String,
    val country: String
)

fun formatBusinessFields(fields: BusinessFields): String {
    return listOf(
        fields.companyName,
        fields.industry,
        fields.phone,
        fields.email,
        fields.address,
        fields.city,
        fields.country
    ).filter { it.isNotBlank() }
        .joinToString(separator = "\n")
}

/*
@Preview
@Composable
private fun EventInputPreview() {
    CodeAppTheme {
        BusinessInputScreen()
    }
}
*/
