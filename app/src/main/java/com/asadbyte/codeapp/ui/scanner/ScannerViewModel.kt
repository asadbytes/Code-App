package com.asadbyte.codeapp.ui.scanner

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asadbyte.codeapp.data.HistoryItem
import com.asadbyte.codeapp.data.HistoryRepository
import com.asadbyte.codeapp.data.ItemType
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Data class to hold the UI state for the scanner screen
data class ScannerUiState(
    val scannedCode: String? = null,
    val capturedBitmap: Bitmap? = null
)

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState = _uiState.asStateFlow()

    // Function to be called when a QR code is successfully scanned
    fun onQrCodeScanned(code: String, bitmap: Bitmap) {
        viewModelScope.launch {
            // Save the scanned item to the database
            repository.insertItem(HistoryItem(content = code, type = ItemType.SCAN))
        }
        _uiState.value = ScannerUiState(scannedCode = code, capturedBitmap = bitmap)
    }

    // Function to reset the state, e.g., after navigating away
    fun resetState() {
        _uiState.value = ScannerUiState()
    }
}