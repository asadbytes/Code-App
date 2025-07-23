package com.asadbyte.codeapp.ui.generator

import android.graphics.Bitmap
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

@HiltViewModel
class GeneratorViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _inputText = MutableStateFlow("")
    val inputText = _inputText.asStateFlow()

    fun onInputTextChanged(newText: String) {
        _inputText.value = newText
    }

    fun generateQrCode(content: String): Bitmap? {
        return try {
            viewModelScope.launch {
                // Save the generated item to the database
                repository.insertItem(HistoryItem(content = content, type = ItemType.GENERATE))
            }
            val barcodeEncoder = BarcodeEncoder()
            // Generate the bitmap from the content string
            barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 400, 400)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}