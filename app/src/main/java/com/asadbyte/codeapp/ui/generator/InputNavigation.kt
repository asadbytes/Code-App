package com.asadbyte.codeapp.ui.generator

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.asadbyte.codeapp.ui.Screen
import com.asadbyte.codeapp.ui.adsMob.AdViewModel
import com.asadbyte.codeapp.ui.generator.input.BusinessInputScreen
import com.asadbyte.codeapp.ui.generator.input.ContactInputScreen
import com.asadbyte.codeapp.ui.generator.input.EventInputScreen
import com.asadbyte.codeapp.ui.generator.input.GenerateInputHandler
import com.asadbyte.codeapp.ui.generator.input.WifiInputScreen
import com.asadbyte.codeapp.ui.generator.input.inputCardData
import com.asadbyte.codeapp.ui.scanner.NewResultScreen
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
    navigateToInputScreen: (String) -> Any,
    onNavigateBackFromInput: () -> Boolean,
    adViewModel: AdViewModel,
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
                GenerateMainScreen(
                    onSettingsClick = { navController.navigate(Screen.Settings.route) },
                    navigateToInputScreen = navigateToInputScreen,
                    adViewModel = adViewModel
                )
            }
        }
        composable(InputScreens.TEXT) {
            GenerateInputHandler(
                cardData = inputCardData["text"]!!,
                onNavigateBack = { onNavigateBackFromInput() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                },
                adViewModel = adViewModel
            )
        }
        composable(InputScreens.WEBSITE) {
            GenerateInputHandler(
                cardData = inputCardData["website"]!!,
                onNavigateBack = { onNavigateBackFromInput() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                },
                adViewModel = adViewModel
            )
        }

        composable(InputScreens.LOCATION) {
            GenerateInputHandler(
                cardData = inputCardData["location"]!!,
                onNavigateBack = { onNavigateBackFromInput() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                },
                adViewModel = adViewModel
            )
        }

        composable(InputScreens.WIFI) {
            WifiInputScreen(
                onNavigateBack = { onNavigateBackFromInput() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                },
                adViewModel = adViewModel
            )
        }

        composable(InputScreens.BUSINESS) {
            BusinessInputScreen(
                onNavigateBack = { onNavigateBackFromInput() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                },
                adViewModel = adViewModel
            )
        }

        composable(InputScreens.CONTACT) {
            ContactInputScreen(
                onNavigateBack = { onNavigateBackFromInput() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                },
                adViewModel = adViewModel
            )
        }

        composable(InputScreens.EVENT) {
            EventInputScreen(
                onNavigateBack = { onNavigateBackFromInput() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                },
                adViewModel = adViewModel
            )
        }

        composable(InputScreens.EMAIL) {
            GenerateInputHandler(
                cardData = inputCardData["email"]!!,
                onNavigateBack = { onNavigateBackFromInput() },
                onQrCodeGenerated = { generateId ->
                    navController.navigate("input_result/$generateId")
                },
                adViewModel = adViewModel
            )
        }
        composable(InputScreens.PHONE) {
            GenerateInputHandler(
                cardData = inputCardData["phone"]!!,
                onNavigateBack = { onNavigateBackFromInput() },
                onQrCodeGenerated = { generateId -> navController.navigate("input_result/$generateId")
                },
                adViewModel = adViewModel
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




val exclusiveInputScreens = setOf(
    InputScreens.TEXT,
    InputScreens.WEBSITE,
    InputScreens.WIFI,
    InputScreens.EVENT,
    InputScreens.CONTACT,
    InputScreens.BUSINESS,
    InputScreens.LOCATION,
    InputScreens.EMAIL,
    InputScreens.PHONE
)