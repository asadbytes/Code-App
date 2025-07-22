package com.asadbyte.codeapp.presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// Data class to hold the UI state for the scanner screen
data class ScannerUiState(
    val scannedCode: String? = null,
    val capturedBitmap: Bitmap? = null
)

class ScannerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState = _uiState.asStateFlow()

    // Function to be called when a QR code is successfully scanned
    fun onQrCodeScanned(code: String, bitmap: Bitmap) {
        _uiState.value = ScannerUiState(scannedCode = code, capturedBitmap = bitmap)
    }

    // Function to reset the state, e.g., after navigating away
    fun resetState() {
        _uiState.value = ScannerUiState()
    }
}