package com.asadbyte.codeapp.ui.generator.input

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.asadbyte.codeapp.data.HistoryItem
import com.asadbyte.codeapp.ui.Screen
import com.asadbyte.codeapp.ui.generator.GenerateHome
import com.asadbyte.codeapp.ui.others.NewResultScreen
import com.asadbyte.codeapp.ui.others.QrCodeMain

object InputScreens {
    const val HOME = "input_home"
    const val TEXT = "input_text"
    const val WEBSITE = "input_website"
    const val WIFI = "input_wifi"
    const val EVENT = "input_event"
    const val CONTACT = "input_contact"
    const val BUSINESS = "input_business"
    const val LOCATION = "input_location"
    const val WHATSAPP = "input_whatsapp"
    const val TWITTER = "input_twitter"
    const val EMAIL = "input_email"
    const val INSTAGRAM = "input_instagram"
    const val PHONE = "input_phone"
    const val RESULT = "input_result"
}

fun NavGraphBuilder.inputGraph(
    navController: NavController,
) {
    Log.d("input_graph", "entered input graph")
    navigation(
        route = "input_graph",
        startDestination = InputScreens.HOME
    ) {
        composable(InputScreens.HOME) {
            QrCodeMain(
                onGenerateClick = {  },
                onScannerClick = { navController.navigate(Screen.Scanner.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) }
            ) {
                GenerateHome(
                    onSettingsClick = { navController.navigate(Screen.Settings.route) },
                    navController = navController
                )
            }
        }
        composable(InputScreens.TEXT) {
            GenerateInputHandler(
                cardData = inputCardData["text"]!!,
                onNavigateBack = { navController.popBackStack() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                }
            )
        }
        composable(InputScreens.WEBSITE) {
            GenerateInputHandler(
                cardData = inputCardData["website"]!!,
                onNavigateBack = { navController.popBackStack() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                }
            )
        }

        composable(InputScreens.LOCATION) {
            GenerateInputHandler(
                cardData = inputCardData["location"]!!,
                onNavigateBack = { navController.popBackStack() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                }
            )
        }

        composable(InputScreens.WIFI) {
            WifiInputScreen(
                onNavigateBack = { navController.popBackStack() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                }
            )
        }

        composable(InputScreens.BUSINESS) {
            BusinessInputScreen(
                onNavigateBack = { navController.popBackStack() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                }
            )
        }

        composable(InputScreens.CONTACT) {
            ContactInputScreen(
                onNavigateBack = { navController.popBackStack() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                }
            )
        }

        composable(InputScreens.EVENT) {
            EventInputScreen(
                onNavigateBack = { navController.popBackStack() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                }
            )
        }
        composable(InputScreens.WHATSAPP) {
            GenerateInputHandler(
                cardData = inputCardData["whatsapp"]!!,
                onNavigateBack = { navController.popBackStack() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                }
            )
        }
        composable(InputScreens.TWITTER) {
            GenerateInputHandler(
                cardData = inputCardData["twitter"]!!,
                onNavigateBack = { navController.popBackStack() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                }
            )
        }
        composable(InputScreens.EMAIL) {
            GenerateInputHandler(
                cardData = inputCardData["email"]!!,
                onNavigateBack = { navController.popBackStack() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                }
            )
        }
        composable(InputScreens.INSTAGRAM) {
            GenerateInputHandler(
                cardData = inputCardData["instagram"]!!,
                onNavigateBack = { navController.popBackStack() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                }
            )
        }
        composable(InputScreens.PHONE) {
            GenerateInputHandler(
                cardData = inputCardData["phone"]!!,
                onNavigateBack = { navController.popBackStack() },
                onQrCodeGenerated = { generateId -> navController.navigate("input_result/$generateId")
                }
            )
        }

        composable(
            InputScreens.RESULT + "/{generateId}",
            arguments = listOf(navArgument("generateId") { type = NavType.StringType })
        ) {
            val generateId = navController.currentBackStackEntry?.arguments?.getString("generateId")
            Log.d("input_graph", "entered result comp with id: $generateId")
            if (generateId != null) {
                NewResultScreen(
                    generateId = generateId.toLong(),
                    onNavigateBack = {
                        navController.popBackStack()
                        Log.d("input_graph", "navigating back from result comp")
                    }
                )
            }
        }
    }
}