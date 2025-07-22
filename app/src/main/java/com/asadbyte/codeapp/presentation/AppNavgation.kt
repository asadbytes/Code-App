package com.asadbyte.codeapp.presentation

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.presentation.generator.GeneratorInputScreen
import com.asadbyte.codeapp.presentation.generator.GeneratorResultScreen
import com.asadbyte.codeapp.presentation.scanner.ScannerResultScreen
import com.asadbyte.codeapp.presentation.scanner.ScannerScreen
import java.net.URLEncoder

sealed class Screen(val route: String, val label: String, @DrawableRes val icon: Int) {
    object Scanner : Screen("scanner", "Scan", R.drawable.qr_code_scanner)
    object Generator : Screen("generator", "Generate", R.drawable.ic_create)
}

val bottomNavItems = listOf(Screen.Scanner, Screen.Generator)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // A simple in-memory cache for passing bitmaps
    val bitmapCache = remember { mutableMapOf<String, Bitmap>() }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(painterResource(screen.icon), contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Scanner.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Scanner Screen
            composable(Screen.Scanner.route) {
                ScannerScreen(
                    onResult = { bitmap, content ->
                        val key = "scan_bitmap_${System.currentTimeMillis()}"
                        bitmapCache[key] = bitmap
                        val encodedContent = URLEncoder.encode(content, "UTF-8")
                        navController.navigate("scan_result/$key/$encodedContent")
                    }
                )
            }
            // Scan Result Screen
            composable("scan_result/{bitmap_key}/{content}") { backStackEntry ->
                val bitmapKey = backStackEntry.arguments?.getString("bitmap_key")
                val content = backStackEntry.arguments?.getString("content")
                val bitmap = bitmapCache[bitmapKey]

                if (bitmap != null && content != null) {
                    ScannerResultScreen(bitmap = bitmap, scannedContent = content) {
                        bitmapCache.remove(bitmapKey)
                        navController.popBackStack()
                    }
                }
            }

            // Generator Input Screen
            composable(Screen.Generator.route) {
                GeneratorInputScreen(onQrCodeGenerated = { bitmap ->
                    val key = "gen_bitmap_${System.currentTimeMillis()}"
                    bitmapCache[key] = bitmap
                    navController.navigate("generator_result/$key")
                })
            }
            // Generator Result Screen
            composable("generator_result/{bitmap_key}") { backStackEntry ->
                val bitmapKey = backStackEntry.arguments?.getString("bitmap_key")
                val bitmap = bitmapCache[bitmapKey]

                if (bitmap != null) {
                    GeneratorResultScreen(bitmap = bitmap) {
                        bitmapCache.remove(bitmapKey)
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}