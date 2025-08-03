package com.asadbyte.codeapp

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    lateinit var preferences: QRScannerPreferences
        private set

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        preferences = QRScannerPreferences.getInstance(this)
    }
}