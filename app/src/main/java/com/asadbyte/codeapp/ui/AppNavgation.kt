package com.asadbyte.codeapp.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.settings.SettingsScreen
import com.asadbyte.codeapp.ui.detail.DetailScreen
import com.asadbyte.codeapp.ui.generator.InputScreens
import com.asadbyte.codeapp.ui.generator.inputGraph
import com.asadbyte.codeapp.ui.history.HistoryScreen
import com.asadbyte.codeapp.ui.history.HistoryViewModel
import com.asadbyte.codeapp.ui.onboarding.OnboardingPreferences
import com.asadbyte.codeapp.ui.others.QrCodeMain
import com.asadbyte.codeapp.ui.onboarding.StartScreen
import com.asadbyte.codeapp.ui.scanner.NewResultScreen
import com.asadbyte.codeapp.ui.scanner.ScannerScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

sealed class Screen(val route: String, val label: String, @DrawableRes val icon: Int) {
    data object StartScreen : Screen("start_screen", "Start", R.drawable.ic_start_screen_qrcode)
    data object Scanner : Screen("scanner", "Scan", R.drawable.qr_code_scanner)
    data object GeneratorHome : Screen("generator_home", "Generate", R.drawable.ic_create)
    data object Generator : Screen("generator", "Generate", R.drawable.ic_create)
    data object History : Screen("history", "History", R.drawable.outline_history_24)
    data object Settings : Screen("settings", "Settings", R.drawable.ic_generate_settings)
    data object GeneratorHolder : Screen("generator_holder", "Generate", R.drawable.ic_create)
}

val bottomNavItems = listOf(Screen.Scanner, Screen.Generator, Screen.History)


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val bitmapCache = remember { mutableMapOf<String, Bitmap>() }
    val historyViewModel: HistoryViewModel = hiltViewModel()
    val historyItems by historyViewModel.history.collectAsState()

    val context = LocalContext.current
    val onboardingPrefs = remember { OnboardingPreferences.getInstance(context) }

    val startDestination = if (onboardingPrefs.hasSeenStartScreen()) {
        "input_graph"
    } else {
        Screen.StartScreen.route
    }


    val scope = rememberCoroutineScope()
    val navigationMutex = remember { Mutex() }

    // 2. DEFINE THE UPDATED NAVIGATION LOGIC
    val navigateToInputScreen = { destinationRoute: String ->
        scope.launch {
            if (navigationMutex.tryLock()) {
                // Lock acquired. Now, set up the failsafe and navigate.

                // FAILSAFE WATCHDOG: Launch a task to auto-unlock after a delay.
                // This prevents the app from getting stuck if navigation fails.
                scope.launch {
                    delay(2500L) // 2.5-second timeout
                    if (navigationMutex.isLocked) {
                        Log.w("NavigationFailsafe", "Navigation likely failed or is taking too long. Forcing unlock.")
                        navigationMutex.unlock()
                    }
                }

                // PROCEED WITH NAVIGATION
                navController.navigate(destinationRoute)
            } else {
                Log.d("Navigation", "Blocked by Mutex. Navigation already in progress.")
            }
        }
    }

    // 3. DEFINE THE UPDATED BACK NAVIGATION
    val onNavigateBackFromInput = {
        // Only unlock if the mutex is currently locked to avoid errors.
        if (navigationMutex.isLocked) {
            navigationMutex.unlock()
        }
        navController.popBackStack()
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.StartScreen.route) {
            StartScreen(
                onArrowClick = {
                    // Mark intro as seen
                    onboardingPrefs.setHasSeenStartScreen()

                    // Navigate and remove StartScreen from backstack
                    navController.navigate(Screen.Scanner.route) {
                        popUpTo(Screen.StartScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Scanner Screen
        composable(Screen.Scanner.route) {
            QrCodeMain(
                onGenerateClick = { navController.navigate("input_graph") {
                    popUpTo(Screen.Scanner.route) {
                        inclusive = true
                    }
                } },
                onScannerClick = { },
                onHistoryClick = { navController.navigate(Screen.History.route) {
                    popUpTo(Screen.Scanner.route) {
                        inclusive = true
                    }
                } }
            ) {
                ScannerScreen(
                    onResult = { scannerId ->
                        navController.navigate("scan_result/$scannerId")
                    }
                )
            }
        }
        // Scan Result Screen
        composable("scan_result/{scannerId}") { backStackEntry ->
            val scannerId = backStackEntry.arguments?.getString("scannerId")

            if (scannerId != null) {
                historyItems.find { it.id == scannerId.toLong() }
                NewResultScreen(
                    generateId = scannerId.toLong(),
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.History.route) {
            QrCodeMain(
                onGenerateClick = { navController.navigate("input_graph") {
                    popUpTo(Screen.History.route) {
                        inclusive = true
                    }
                } },
                onScannerClick = { navController.navigate(Screen.Scanner.route) {
                    popUpTo(Screen.History.route) {
                        inclusive = true
                    }
                } },
                onHistoryClick = { }
            ) {
                HistoryScreen(
                    onSettingsClick = { navController.navigate(Screen.Settings.route) },
                    onItemClick = { item ->
                        navController.navigate("detail/${item.id}")
                        Log.d("HistoryHome", "onItemClick: ${item.id}")
                    }
                )
            }
        }
        composable(
            route = "detail/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            if (!itemId.isNullOrBlank()) {
                val item = historyItems.find { it.id == itemId.toLong() }
                DetailScreen(
                    item = item,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.Settings.route) {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }

        inputGraph(
            navController = navController,
            navigateToInputScreen = navigateToInputScreen,
            onNavigateBackFromInput = { onNavigateBackFromInput() }
        )
    }
}