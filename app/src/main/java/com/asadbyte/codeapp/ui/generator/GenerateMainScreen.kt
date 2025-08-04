package com.asadbyte.codeapp.ui.generator

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.ui.adsMob.AdViewModel
import com.asadbyte.codeapp.ui.adsMob.NativeAdView
import com.asadbyte.codeapp.ui.others.disableMultiTouch
import com.asadbyte.codeapp.ui.others.singleClickable
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.ItimFont
import com.asadbyte.codeapp.ui.theme.MyYellow

@Composable
fun GenerateMainScreen(
    onSettingsClick: () -> Unit,
    navigateToInputScreen: (String) -> Any,
    adViewModel: AdViewModel
) {
    val adUiState by adViewModel.uiState.collectAsState()
    /*LaunchedEffect(Unit) {
        adViewModel.loadNativeAd()
    }*/

    // KEY FIX: Use LazyVerticalGrid as the main container for the whole screen.
    // This makes the entire content area (header, icons, ad) scrollable together.
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .background(Gray10)
            .disableMultiTouch(),
        // Consolidate padding and spacing for a clean, uniform look.
        // Added bottom padding for space after the last item.
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // ITEM 1: Top Bar (Header)
        // Use 'span' to make this item occupy all 3 columns of the grid.
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier.padding(bottom = 5.dp), // Space between header and grid
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Generate QR",
                    fontFamily = ItimFont,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = "Settings",
                    tint = MyYellow,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onSettingsClick() }
                )
            }
        }

        // ITEM 2: Menu Icons
        // The list of icons will flow naturally within the grid.
        items(generateHomeList) { homeItem ->
            Image(
                painter = painterResource(homeItem.icon),
                contentDescription = null,
                modifier = Modifier
                    .singleClickable { navigateToInputScreen(homeItem.path) }
                    .aspectRatio(1f) // Ensures icons are always square
            )
        }

        // ITEM 3: Ad View
        // The ad is now the last item in the scrollable grid, also spanning all columns.
        // It will only be visible after scrolling past the menu icons if they fill the screen.
        item(span = { GridItemSpan(maxLineSpan) }) {
            val nativeAd = adUiState.nativeAd
            if (nativeAd != null) {
                /*NativeAdView(
                    nativeAd = nativeAd,
                    modifier = Modifier.fillMaxWidth()
                )*/
            } else if (adUiState.isNativeLoading) {
                // Optional: Show a placeholder while the ad is loading
                /*Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp), // Approx height of your ad
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }*/
            }
        }
        item {
            Spacer(modifier = Modifier.height(130.dp))
        }
    }
}

val generateHomeList = listOf(
    GenerateHomeItem(
        icon = R.drawable.ic_generate_text,
        path =  InputScreens.TEXT
    ),
    GenerateHomeItem(
        icon = R.drawable.ic_generate_website,
        path =  InputScreens.WEBSITE
    ),
    GenerateHomeItem(
        icon = R.drawable.ic_generate_wifi,
        path =  InputScreens.WIFI
    ),
    GenerateHomeItem(
        icon = R.drawable.ic_generate_event,
        path =  InputScreens.EVENT
    ),
    GenerateHomeItem(
        icon = R.drawable.ic_generate_contact,
        path =  InputScreens.CONTACT
    ),
    GenerateHomeItem(
        icon = R.drawable.ic_generate_business,
        path =  InputScreens.BUSINESS
    ),
    GenerateHomeItem(
        icon = R.drawable.ic_generate_location,
        path =  InputScreens.LOCATION
    ),
    GenerateHomeItem(
        icon = R.drawable.ic_generate_email,
        path =  InputScreens.EMAIL
    ),
    GenerateHomeItem(
        icon = R.drawable.ic_generate_telephone,
        path =  InputScreens.PHONE
    ),
)

data class GenerateHomeItem(
    val icon: Int,
    val path: String
)
