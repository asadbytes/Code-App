package com.asadbyte.codeapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    lateinit var preferences: QRScannerPreferences
        private set
    override fun onCreate() {
        super.onCreate()
        preferences = QRScannerPreferences.getInstance(this)
    }
}