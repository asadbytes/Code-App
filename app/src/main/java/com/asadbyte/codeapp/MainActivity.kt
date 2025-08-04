package com.asadbyte.codeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asadbyte.codeapp.ui.AppNavigation
import com.asadbyte.codeapp.ui.adsMob.AdViewModel
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val adViewModel: AdViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*val splash = installSplashScreen()
        splash.setKeepOnScreenCondition {
            adViewModel.uiState.value.isAppLoading
        }*/
        setContent {
            /*adViewModel.handleInitialAppLoad(this)
            val uiState by adViewModel.uiState.collectAsStateWithLifecycle()*/

            CodeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        adViewModel = adViewModel
                    )
                }
                /*if(!uiState.isAppLoading) {
                }*/
            }
        }
    }
}