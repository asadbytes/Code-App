package com.asadbyte.codeapp.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.settings.SettingsScreen
import com.asadbyte.codeapp.ui.detail.DetailScreen
import com.asadbyte.codeapp.ui.generator.input.inputGraph
import com.asadbyte.codeapp.ui.history.HistoryScreen
import com.asadbyte.codeapp.ui.history.HistoryViewModel
import com.asadbyte.codeapp.ui.others.QrCodeMain
import com.asadbyte.codeapp.ui.others.StartScreen
import com.asadbyte.codeapp.ui.scanner.NewResultScreen
import com.asadbyte.codeapp.ui.scanner.ScannerScreen

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
    // A simple in-memory cache for passing bitmaps
    val bitmapCache = remember { mutableMapOf<String, Bitmap>() }
    val historyViewModel: HistoryViewModel = hiltViewModel()
    val historyItems by historyViewModel.history.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.StartScreen.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.StartScreen.route) {
            StartScreen(
                onArrowClick = { navController.navigate(Screen.Scanner.route) }
            )
        }
        // Scanner Screen
        composable(Screen.Scanner.route) {
            QrCodeMain(
                onGenerateClick = { navController.navigate("input_graph") },
                onScannerClick = { },
                onHistoryClick = { navController.navigate(Screen.History.route) }
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
                onGenerateClick = { navController.navigate("input_graph") },
                onScannerClick = { navController.navigate(Screen.Scanner.route) },
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
            Log.d("HistoryHome", "ItemId recieved in detail screen: $itemId")
            if (!itemId.isNullOrBlank()) {
                val item = historyItems.find { it.id == itemId.toLong() }
                Log.d("HistoryHome", "ItemId sent to viewmodel: ${itemId.toLong()}")
                // Get item by ID from your data source
                QrCodeMain(
                    onGenerateClick = { navController.navigate("input_graph") },
                    onScannerClick = { navController.navigate(Screen.Scanner.route) },
                    onHistoryClick = { navController.navigate(Screen.History.route) }
                ) {
                    DetailScreen(
                        item = item,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }

        composable(Screen.Settings.route) {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }

        inputGraph(navController = navController)
    }
}