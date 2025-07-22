package com.asadbyte.codeapp.presentation

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.net.URLEncoder

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // A simple in-memory cache for passing the bitmap
    val bitmapCache = remember { mutableMapOf<String, Bitmap>() }

    NavHost(navController = navController, startDestination = "scanner") {
        composable("scanner") {
            ScannerScreen(
                onResult = { bitmap, content ->
                    val key = "bitmap_${System.currentTimeMillis()}"
                    bitmapCache[key] = bitmap
                    val encodedContent = URLEncoder.encode(content, "UTF-8")
                    navController.navigate("result/$key/$encodedContent")
                }
            )
        }
        composable("result/{bitmap_key}/{content}") { backStackEntry ->
            val bitmapKey = backStackEntry.arguments?.getString("bitmap_key")
            val content = backStackEntry.arguments?.getString("content")
            val bitmap = bitmapCache[bitmapKey]

            if (bitmap != null && content != null) {
                ResultScreen(bitmap = bitmap, scannedContent = content) {
                    navController.popBackStack()
                    bitmapCache.remove(bitmapKey) // Clean up the cache
                }
            }
        }
    }
}