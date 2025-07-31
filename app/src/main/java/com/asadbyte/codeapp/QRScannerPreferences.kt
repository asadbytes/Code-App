package com.asadbyte.codeapp

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QRScannerPreferences(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "qr_scanner_preferences"
        private const val KEY_BEEP_SOUND = "beep_sound"
        private const val KEY_VIBRATION = "vibration"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val DEFAULT_BEEP_SOUND = true
        private const val DEFAULT_VIBRATION = true
        private const val VIBRATION_DURATION = 500L // milliseconds

        @Volatile
        private var INSTANCE: QRScannerPreferences? = null

        fun getInstance(context: Context): QRScannerPreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: QRScannerPreferences(context.applicationContext).also {
                    INSTANCE = it
                    it.initializePreferences()
                }
            }
        }
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Compose State for UI
    private val _beepSoundState = mutableStateOf(DEFAULT_BEEP_SOUND)
    val beepSoundState: State<Boolean> = _beepSoundState

    private val _vibrationState = mutableStateOf(DEFAULT_VIBRATION)
    val vibrationState: State<Boolean> = _vibrationState

    // StateFlow for observing changes
    private val _preferencesFlow = MutableStateFlow(PreferencesData(DEFAULT_BEEP_SOUND, DEFAULT_VIBRATION))
    val preferencesFlow: StateFlow<PreferencesData> = _preferencesFlow.asStateFlow()

    /**
     * Initialize preferences on app startup
     * This should be called once when the app starts
     */
    fun initializePreferences() {
        if (isFirstLaunch()) {
            // First time launching the app - save defaults
            saveDefaultPreferences()
            markFirstLaunchComplete()
        } else {
            // Load existing preferences
            loadExistingPreferences()
        }
    }

    /**
     * Check if this is the first launch of the app
     */
    private fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true)
    }

    /**
     * Mark first launch as complete
     */
    private fun markFirstLaunchComplete() {
        sharedPreferences.edit()
            .putBoolean(KEY_FIRST_LAUNCH, false)
            .apply()
    }

    /**
     * Save default preferences on first launch
     */
    private fun saveDefaultPreferences() {
        try {
            sharedPreferences.edit()
                .putBoolean(KEY_BEEP_SOUND, DEFAULT_BEEP_SOUND)
                .putBoolean(KEY_VIBRATION, DEFAULT_VIBRATION)
                .apply()

            // Update state objects
            _beepSoundState.value = DEFAULT_BEEP_SOUND
            _vibrationState.value = DEFAULT_VIBRATION
            updatePreferencesFlow()

            android.util.Log.d("QRPreferences", "Default preferences saved")
        } catch (e: Exception) {
            android.util.Log.e("QRPreferences", "Failed to save default preferences", e)
        }
    }

    /**
     * Load existing preferences from storage
     */
    private fun loadExistingPreferences() {
        try {
            val beepSound = getBeepSound()
            val vibration = getVibration()

            // Update state objects with loaded values
            _beepSoundState.value = beepSound
            _vibrationState.value = vibration
            updatePreferencesFlow()

            android.util.Log.d("QRPreferences", "Existing preferences loaded - Beep: $beepSound, Vibration: $vibration")
        } catch (e: Exception) {
            android.util.Log.e("QRPreferences", "Failed to load existing preferences", e)
            // Fallback to defaults if loading fails
            saveDefaultPreferences()
        }
    }

    // Data class for preferences
    data class PreferencesData(
        val beepSound: Boolean,
        val vibration: Boolean
    )

    /**
     * Get beep sound preference
     */
    fun getBeepSound(): Boolean {
        return sharedPreferences.getBoolean(KEY_BEEP_SOUND, DEFAULT_BEEP_SOUND)
    }

    /**
     * Get vibration preference
     */
    fun getVibration(): Boolean {
        return sharedPreferences.getBoolean(KEY_VIBRATION, DEFAULT_VIBRATION)
    }

    /**
     * Set beep sound preference
     */
    fun setBeepSound(enabled: Boolean): Boolean {
        return try {
            sharedPreferences.edit()
                .putBoolean(KEY_BEEP_SOUND, enabled)
                .apply()
            _beepSoundState.value = enabled
            updatePreferencesFlow()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Set vibration preference
     */
    fun setVibration(enabled: Boolean): Boolean {
        return try {
            sharedPreferences.edit()
                .putBoolean(KEY_VIBRATION, enabled)
                .apply()
            _vibrationState.value = enabled
            updatePreferencesFlow()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Toggle beep sound preference
     */
    fun toggleBeepSound(): Boolean {
        val newValue = !getBeepSound()
        setBeepSound(newValue)
        return newValue
    }

    /**
     * Toggle vibration preference
     */
    fun toggleVibration(): Boolean {
        val newValue = !getVibration()
        setVibration(newValue)
        return newValue
    }

    /**
     * Update multiple preferences at once
     */
    fun updatePreferences(beepSound: Boolean? = null, vibration: Boolean? = null): Boolean {
        return try {
            val editor = sharedPreferences.edit()

            beepSound?.let {
                editor.putBoolean(KEY_BEEP_SOUND, it)
                _beepSoundState.value = it
            }

            vibration?.let {
                editor.putBoolean(KEY_VIBRATION, it)
                _vibrationState.value = it
            }

            editor.apply()
            updatePreferencesFlow()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Reset all preferences to defaults
     */
    fun resetToDefaults(): Boolean {
        return try {
            sharedPreferences.edit()
                .putBoolean(KEY_BEEP_SOUND, DEFAULT_BEEP_SOUND)
                .putBoolean(KEY_VIBRATION, DEFAULT_VIBRATION)
                .apply()

            _beepSoundState.value = DEFAULT_BEEP_SOUND
            _vibrationState.value = DEFAULT_VIBRATION
            updatePreferencesFlow()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Check if beep sound is enabled
     */
    fun isBeepEnabled(): Boolean = getBeepSound()

    /**
     * Check if vibration is enabled
     */
    fun isVibrationEnabled(): Boolean = getVibration()

    /**
     * Play beep sound if enabled (call this when QR code is scanned)
     */
    fun playBeepIfEnabled() {
        if (isBeepEnabled()) {
            playBeepSound()
        }
    }

    /**
     * Vibrate if enabled (call this when QR code is scanned)
     */
    fun vibrateIfEnabled() {
        Log.d("QRPreferences", "vibrateIfEnabled called, isEnabled: ${isVibrationEnabled()}")
        if (isVibrationEnabled()) {
            Log.d("QRPreferences", "About to vibrate")
            vibrate()
        }
    }

    /**
     * Execute both beep and vibration if enabled (convenience method)
     */
    fun executeFeedbackIfEnabled() {
        playBeepIfEnabled()
        vibrateIfEnabled()
    }

    /**
     * Private method to play beep sound
     */
    private fun playBeepSound() {
        try {
            // Option 1: Use system notification sound
            /*val notification = android.media.RingtoneManager.getDefaultUri(
                android.media.RingtoneManager.TYPE_NOTIFICATION
            )
            val ringtone = android.media.RingtoneManager.getRingtone(context, notification)
            ringtone?.play()*/

            // Option 2: Use MediaPlayer with custom sound (uncomment if you have a custom beep sound)
            val mediaPlayer = MediaPlayer.create(context, R.raw.qrcode_beep_sound)
            mediaPlayer?.start()
            mediaPlayer?.setOnCompletionListener { it.release() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Private method to vibrate device
     */
    private fun vibrate() {
        try {
            val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(VIBRATION_DURATION, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(VIBRATION_DURATION)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Update the preferences flow
     */
    private fun updatePreferencesFlow() {
        _preferencesFlow.value = PreferencesData(getBeepSound(), getVibration())
    }

    /**
     * Get all preferences as data class
     */
    fun getAllPreferences(): PreferencesData {
        return PreferencesData(getBeepSound(), getVibration())
    }
}
