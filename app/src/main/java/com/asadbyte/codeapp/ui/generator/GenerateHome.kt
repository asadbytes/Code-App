package com.asadbyte.codeapp.ui.generator

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.ui.generator.input.InputScreens
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.ItimFont
import com.asadbyte.codeapp.ui.theme.MyYellow

@Composable
fun GenerateMainScreen(
    onSettingsClick: () -> Unit,
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray10)
    ) {

        Spacer(modifier = Modifier.height(5.dp))
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // Use symmetrical horizontal padding for balance.
                .padding(horizontal = 20.dp, vertical = 12.dp),
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
        Spacer(modifier = Modifier.height(5.dp))
        // 3-column Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            // KEY FIX: Use weight(1f) to fill remaining space in the Column.
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            // Consolidate all spacing into contentPadding and arrangements.
            // Using a single value for arrangements creates a more uniform grid.
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(generateHomeList) { homeItem ->
                // aspectRatio(1f) is great for ensuring items are always square.
                Image(
                    painter = painterResource(homeItem.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { navController.navigate(homeItem.path) }
                        .aspectRatio(1f)
                )
            }
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
        icon = R.drawable.ic_generate_whatsapp,
        path =  InputScreens.WHATSAPP
    ),
    GenerateHomeItem(
        icon = R.drawable.ic_generate_email,
        path =  InputScreens.EMAIL
    ),
    GenerateHomeItem(
        icon = R.drawable.ic_generate_twitter,
        path =  InputScreens.TWITTER
    ),
    GenerateHomeItem(
        icon = R.drawable.ic_generate_instagram,
        path =  InputScreens.INSTAGRAM
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

@Preview
@Composable
private fun GenerateHomePreview() {
    CodeAppTheme {
        GenerateMainScreen(
            onSettingsClick = {},
            navController = NavController(LocalContext.current)
        )

    }
}