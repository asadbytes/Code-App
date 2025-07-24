package com.asadbyte.codeapp.ui

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.ui.detail.DetailScreen
import com.asadbyte.codeapp.ui.generator.GenerateHome
import com.asadbyte.codeapp.ui.generator.GeneratorInputScreen
import com.asadbyte.codeapp.ui.generator.GeneratorResultScreen
import com.asadbyte.codeapp.ui.history.HistoryScreen
import com.asadbyte.codeapp.ui.scanner.ScannerResultScreen
import com.asadbyte.codeapp.ui.scanner.ScannerScreen
import com.asadbyte.codeapp.ui.splash.MySplashScreen
import com.asadbyte.codeapp.ui.splash.QrCodeMain
import com.asadbyte.codeapp.ui.splash.StartScreen
import java.net.URLDecoder
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
                    onResult = { bitmap, content ->
                        val key = "scan_bitmap_${System.currentTimeMillis()}"
                        bitmapCache[key] = bitmap
                        val encodedContent = URLEncoder.encode(content, "UTF-8")
                        navController.navigate("scan_result/$key/$encodedContent")
                    }
                )
            }
        }
        // Scan Result Screen
        composable("scan_result/{bitmap_key}/{content}") { backStackEntry ->
            val bitmapKey = backStackEntry.arguments?.getString("bitmap_key")
            val content = backStackEntry.arguments?.getString("content")
            val bitmap = bitmapCache[bitmapKey]

            if (bitmap != null && content != null) {
               QrCodeMain(
                   onGenerateClick = { navController.navigate(Screen.GeneratorHome.route) },
                   onScannerClick = { navController.navigate(Screen.Scanner.route) },
                   onHistoryClick = { navController.navigate(Screen.History.route) }
               ) {
                   ScannerResultScreen(bitmap = bitmap, scannedContent = content) {
                       bitmapCache.remove(bitmapKey)
                       navController.popBackStack()
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
                GeneratorInputScreen(onQrCodeGenerated = { bitmap ->
                    val key = "gen_bitmap_${System.currentTimeMillis()}"
                    bitmapCache[key] = bitmap
                    navController.navigate("generator_result/$key")
                }
                )
            }
        }
        // Generator Result Screen
        composable("generator_result/{bitmap_key}") { backStackEntry ->
            val bitmapKey = backStackEntry.arguments?.getString("bitmap_key")
            val bitmap = bitmapCache[bitmapKey]

            if (bitmap != null) {
                QrCodeMain(
                    onGenerateClick = { navController.navigate(Screen.GeneratorHome.route) },
                    onScannerClick = { navController.navigate(Screen.Scanner.route) },
                    onHistoryClick = { navController.navigate(Screen.History.route) }
                ) {
                    GeneratorResultScreen(bitmap = bitmap) {
                        bitmapCache.remove(bitmapKey)
                        navController.popBackStack()
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
                HistoryScreen(
                    onItemClick = { item ->
                        // URL-encode the content to handle special characters safely
                        val encodedContent = URLEncoder.encode(item.content, "UTF-8")
                        navController.navigate("detail/$encodedContent")
                    }
                )
            }
        }
        composable("detail/{content}") { backStackEntry ->
            val encodedContent = backStackEntry.arguments?.getString("content") ?: ""
            val content = URLDecoder.decode(encodedContent, "UTF-8")
            QrCodeMain(
                onGenerateClick = { navController.navigate(Screen.GeneratorHome.route) },
                onScannerClick = { navController.navigate(Screen.Scanner.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) }
            ) {
                DetailScreen(
                    content = content,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}