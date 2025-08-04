package com.asadbyte.codeapp.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import com.asadbyte.codeapp.QRScannerPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val preferences = QRScannerPreferences.getInstance(context)
    // Expose the preferences flow directly. The UI will collect this.
    val settingsUiState = preferences.preferencesFlow

    fun setVibration(enabled: Boolean) {
        preferences.setVibration(enabled)
    }

    fun setBeep(enabled: Boolean) {
        preferences.setBeepSound(enabled)
    }
}