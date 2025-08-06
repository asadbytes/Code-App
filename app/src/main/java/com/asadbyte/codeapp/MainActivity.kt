package com.asadbyte.codeapp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asadbyte.codeapp.ui.AppNavigation
import com.asadbyte.codeapp.ui.adsMob.AdViewModel
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.InstallStatus
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val adViewModel: AdViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*val splash = installSplashScreen()
        splash.setKeepOnScreenCondition {
            adViewModel.uiState.value.isAppLoading
        }*/
        WindowCompat.setDecorFitsSystemWindows(window, false)
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

@Composable
fun InAppUpdateHandler() {
    val context = LocalContext.current
    val appUpdateManager = AppUpdateManagerFactory.create(context)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // This listener is for flexible updates to know when the download is complete.
    val installStateUpdatedListener = remember {
        InstallStateUpdatedListener { state: InstallState ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                coroutineScope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = "Update downloaded.",
                        actionLabel = "Restart",
                        duration = SnackbarDuration.Indefinite
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        appUpdateManager.completeUpdate()
                    }
                }
            }
        }
    }

    // Register and unregister the listener based on the composable's lifecycle.
    DisposableEffect(appUpdateManager) {
        appUpdateManager.registerListener(installStateUpdatedListener)
        onDispose {
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
    }

    // For Immediate updates, we still use the ActivityResultLauncher.
    val resultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> Toast.makeText(context, "Update successful!", Toast.LENGTH_LONG).show()
            Activity.RESULT_CANCELED -> Toast.makeText(context, "Update canceled.", Toast.LENGTH_LONG).show()
            else -> Toast.makeText(context, "Update failed.", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            val isUpdateAvailable = appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE


            if (isUpdateAvailable) {
                val isImmediateUpdatePossible = appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                val isFlexibleUpdatePossible = appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)

                if (isImmediateUpdatePossible) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        resultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    )
                } else if (isFlexibleUpdatePossible) {
                    // CORRECTED: Pass the activity instance as the second parameter
                    (context as? Activity)?.let {
                        appUpdateManager.startUpdateFlow(
                            appUpdateInfo,
                            it, // Pass the Activity instance here
                            AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                        )
                    }
                }
            }
        }
    }

    // Show the Snackbar
    SnackbarHost(hostState = snackbarHostState)
}