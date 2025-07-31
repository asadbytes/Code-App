package com.asadbyte.codeapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    lateinit var preferences: QRScannerPreferences
        private set

    var isAppColdStarted = true

    override fun onCreate() {
        super.onCreate()
        isAppColdStarted = true
        preferences = QRScannerPreferences.getInstance(this)
    }
}