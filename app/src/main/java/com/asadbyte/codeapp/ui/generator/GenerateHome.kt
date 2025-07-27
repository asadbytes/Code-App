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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.asadbyte.codeapp.ui.Screen
import com.asadbyte.codeapp.ui.generator.input.InputScreens
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.ItimFont

@Composable
fun GenerateHome(
    onSettingsClick: () -> Unit,
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray10)
    ) {

        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Generate QR",
                fontFamily = ItimFont,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_generate_settings),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clickable { onSettingsClick() }
            )
        }

        // 3-column Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalArrangement = Arrangement.spacedBy(35.dp),
            contentPadding = PaddingValues(horizontal = 18.dp)
        ) {
            items(generateHomeList) { homeItem ->
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
        path =  InputScreens.HOME
    ),
    GenerateHomeItem(
        icon = R.drawable.ic_generate_contact,
        path =  InputScreens.HOME
    ),
    GenerateHomeItem(
        icon = R.drawable.ic_generate_business,
        path =  InputScreens.HOME
    ),
    GenerateHomeItem(
        icon = R.drawable.ic_generate_location,
        path =  InputScreens.HOME
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
        GenerateHome(
            onSettingsClick = {},
            navController = NavController(LocalContext.current)
        )

    }
}