package com.asadbyte.codeapp.ui.onboarding

import android.content.Context
import android.content.SharedPreferences

class OnboardingPreferences private constructor(context: Context) {

    companion object {
        private const val PREFS_NAME = "onboarding_preferences"
        private const val KEY_HAS_SEEN_START_SCREEN = "has_seen_start_screen"

        @Volatile
        private var INSTANCE: OnboardingPreferences? = null

        fun getInstance(context: Context): OnboardingPreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: OnboardingPreferences(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun hasSeenStartScreen(): Boolean {
        return sharedPrefs.getBoolean(KEY_HAS_SEEN_START_SCREEN, false)
    }

    fun setHasSeenStartScreen() {
        sharedPrefs.edit().putBoolean(KEY_HAS_SEEN_START_SCREEN, true).apply()
    }
}
