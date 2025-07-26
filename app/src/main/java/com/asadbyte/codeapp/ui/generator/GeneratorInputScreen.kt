package com.asadbyte.codeapp.ui.generator

import android.graphics.Bitmap
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun GeneratorInputScreen(
    onQrCodeGenerated: (Long, Bitmap) -> Unit,
    generatorViewModel: GeneratorViewModel = hiltViewModel()
) {
    val text by generatorViewModel.inputText.collectAsState()
    val uiState by generatorViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { generatorViewModel.onInputTextChanged(it) },
            label = { Text("Enter text to encode") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    // Generate the QR code and trigger the callback
                    generatorViewModel.generateQrCode(text)
                }
            },
            enabled = text.isNotBlank()
        ) {
            Text("Generate QR Code")
        }
    }

    LaunchedEffect(uiState) {
        if (uiState.capturedBitmap != null) {
            onQrCodeGenerated(uiState.generatedId!!, uiState.capturedBitmap!!)
        }

    }
}