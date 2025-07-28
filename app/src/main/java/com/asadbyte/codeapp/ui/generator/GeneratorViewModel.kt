package com.asadbyte.codeapp.ui.generator

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asadbyte.codeapp.data.HistoryItem
import com.asadbyte.codeapp.data.HistoryRepository
import com.asadbyte.codeapp.data.ItemType
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GeneratorUiState(
    val generatedId: Long? = null,
    val generatedCode: String? = null,
    val capturedBitmap: Bitmap? = null
)

@HiltViewModel
class GeneratorViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GeneratorUiState())
    val uiState = _uiState.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText = _inputText.asStateFlow()

    fun onInputTextChanged(newText: String) {
        _inputText.value = newText
    }

    fun generateQrCode(content: String) {
        viewModelScope.launch {
            try {
                viewModelScope.launch {
                    val generatedId = repository.insertItem(HistoryItem(content = content, type = ItemType.GENERATE))
                    _uiState.value = GeneratorUiState(
                        generatedId = generatedId,
                        generatedCode = content,
                        capturedBitmap = getQrCode(content)
                    )
                }
                /*val barcodeEncoder = BarcodeEncoder()
                // Generate the bitmap from the content string
                barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 400, 400)*/
            } catch (e: Exception) {
                Log.e("Scanner", "Database Insert failed", e)
                _uiState.value = GeneratorUiState(generatedCode = content)
            }
        }
    }

    fun getQrCode(content: String): Bitmap? {
        return try {
            val barcodeEncoder = BarcodeEncoder()
            // Generate the bitmap from the content string
            barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 400, 400)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun clearGeneratedQrCode() {
        _uiState.value = _uiState.value.copy(
            capturedBitmap = null,
            generatedId = null
        )
    }
}