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
import com.asadbyte.codeapp.ui.detail.DetailScreenNew
import com.asadbyte.codeapp.ui.generator.GenerateHome
import com.asadbyte.codeapp.ui.generator.GeneratorInputScreen
import com.asadbyte.codeapp.ui.generator.GeneratorResultScreen
import com.asadbyte.codeapp.ui.history.HistoryHomeNew
import com.asadbyte.codeapp.ui.history.HistoryViewModel
import com.asadbyte.codeapp.ui.others.NewResultScreen
import com.asadbyte.codeapp.ui.scanner.ScannerResultScreen
import com.asadbyte.codeapp.ui.scanner.ScannerScreen
import com.asadbyte.codeapp.ui.others.QrCodeMain
import com.asadbyte.codeapp.ui.others.StartScreen
import java.net.URLEncoder

sealed class Screen(val route: String, val label: String, @DrawableRes val icon: Int) {
    data object StartScreen: Screen("start_screen", "Start", R.drawable.ic_start_screen_qrcode)
    data object Scanner : Screen("scanner", "Scan", R.drawable.qr_code_scanner)
    data object GeneratorHome : Screen("generator_home", "Generate", R.drawable.ic_create)
    data object Generator : Screen("generator", "Generate", R.drawable.ic_create)
    data object History : Screen("history", "History", R.drawable.outline_history_24)
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
                onGenerateClick = { navController.navigate(Screen.GeneratorHome.route) },
                onScannerClick = { navController.navigate(Screen.Scanner.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) }
            ) {
                ScannerScreen(
                    onResult = { scannerId, bitmap, content ->
                        Log.d("Scanned Id", "AppNavigation: got this id from onResult: $scannerId")
                        val key = "scan_bitmap_${System.currentTimeMillis()}"
                        bitmapCache[key] = bitmap
                        val encodedContent = URLEncoder.encode(content, "UTF-8")
                        navController.navigate("scan_result/$key/$encodedContent/$scannerId")
                    }
                )
            }
        }
        // Scan Result Screen
        composable("scan_result/{bitmap_key}/{content}/{scannerId}") { backStackEntry ->
            val bitmapKey = backStackEntry.arguments?.getString("bitmap_key")
            val content = backStackEntry.arguments?.getString("content")
            val scannerId = backStackEntry.arguments?.getString("scannerId")
            val bitmap = bitmapCache[bitmapKey]

            if (bitmap != null && content != null) {
               QrCodeMain(
                   onGenerateClick = { navController.navigate(Screen.GeneratorHome.route) },
                   onScannerClick = { navController.navigate(Screen.Scanner.route) },
                   onHistoryClick = { navController.navigate(Screen.History.route) }
               ) {
                   if(scannerId == null) {
                       ScannerResultScreen(bitmap = bitmap, scannedContent = content) {
                           bitmapCache.remove(bitmapKey)
                           navController.popBackStack()
                       }
                   } else {
                       val item = historyItems.find { it.id == scannerId.toLong() }
                       NewResultScreen(
                           item = item,
                           onNavigateBack = { navController.popBackStack() }
                       )
                   }
               }
            }
        }

        composable(Screen.GeneratorHome.route) {
            QrCodeMain(
                onGenerateClick = { navController.navigate(Screen.GeneratorHome.route) },
                onScannerClick = { navController.navigate(Screen.Scanner.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) }
            ) {
                GenerateHome(
                    onSettingsClick = { /* Handle settings click */ },
                    navController = navController
                )
            }
        }
        // Generator Input Screen
        composable(Screen.Generator.route) {
            QrCodeMain(
                onGenerateClick = { navController.navigate(Screen.GeneratorHome.route) },
                onScannerClick = { navController.navigate(Screen.Scanner.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) }
            ) {
                GeneratorInputScreen(onQrCodeGenerated = { generateId, bitmap ->
                    val key = "gen_bitmap_${System.currentTimeMillis()}"
                    bitmapCache[key] = bitmap
                    navController.navigate("generator_result/$key/$generateId")
                }
                )
            }
        }
        // Generator Result Screen
        composable("generator_result/{bitmap_key}/{generateId}") { backStackEntry ->
            val bitmapKey = backStackEntry.arguments?.getString("bitmap_key")
            val generateId = backStackEntry.arguments?.getString("generateId")
            val item = historyItems.find { it.id == generateId?.toLong() }
            val bitmap = bitmapCache[bitmapKey]

            if (bitmap != null) {
                QrCodeMain(
                    onGenerateClick = { navController.navigate(Screen.GeneratorHome.route) },
                    onScannerClick = { navController.navigate(Screen.Scanner.route) },
                    onHistoryClick = { navController.navigate(Screen.History.route) }
                ) {
                    if(item != null){
                        NewResultScreen(
                            item = item,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    } else {
                        GeneratorResultScreen(bitmap = bitmap) {
                            bitmapCache.remove(bitmapKey)
                            navController.popBackStack()
                        }
                    }
                }
            }
        }

        composable(Screen.History.route) {
            QrCodeMain(
                onGenerateClick = { navController.navigate(Screen.GeneratorHome.route) },
                onScannerClick = { navController.navigate(Screen.Scanner.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) }
            ) {
                /*HistoryScreen(
                    onItemClick = { item ->
                        // URL-encode the content to handle special characters safely
                        val encodedContent = URLEncoder.encode(item.content, "UTF-8")
                        navController.navigate("detail/$encodedContent")
                    }
                )*/
                HistoryHomeNew(
                    onSettingsClick = { /* Handle settings click */ },
                    onItemClick = { item ->
                        navController.navigate("detail/${item.id}")
                        Log.d("HistoryHome", "onItemClick: ${item.id}")
                    },
                    navController = navController
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
                    onGenerateClick = { navController.navigate(Screen.GeneratorHome.route) },
                    onScannerClick = { navController.navigate(Screen.Scanner.route) },
                    onHistoryClick = { navController.navigate(Screen.History.route) }
                ) {
                    /*DetailScreen(
                        content = item?.content ?: "no content",
                        onNavigateBack = { navController.popBackStack() }
                    )*/
                    DetailScreenNew(
                        item = item,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }

    }
}