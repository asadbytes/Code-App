package com.asadbyte.codeapp.ui.others

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.asadbyte.codeapp.ui.theme.Gray20
import com.asadbyte.codeapp.ui.theme.Gray30
import com.asadbyte.codeapp.ui.theme.ItimFont

@Composable
fun StartScreen(
    onArrowClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFFDB623))
    ) {
        Image(
            painter = painterResource(R.drawable.qrcode_yellow_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Image(
            painter = painterResource(R.drawable.qrcode_homescreen_top),
            contentDescription = "Next Screen",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
        )
        Image(
            painter = painterResource(R.drawable.qrcode_homescreen_bottom),
            contentDescription = "Next Screen",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 5.dp, horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.size(240.dp))
            Image(
                painter = painterResource(R.drawable.ic_start_screen_qrcode),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.size(170.dp))
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.headlineLarge,
                    fontFamily = ItimFont,
                    color = Gray30
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "Go and enjoy our features for free and make your life easy with us.",
                    fontFamily = ItimFont,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray20,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 25.dp)
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.ic_yellow_arrow),
            contentDescription = "Next Screen", // Good for accessibility
            modifier = Modifier
                .align(Alignment.BottomEnd) // Positions the image in the corner
                .padding(horizontal = 20.dp, vertical = 15.dp) // Adds spacing from the edges
                .size(100.dp) // Give it a defined size
                .clickable { onArrowClick() }
        )
    }
}


@Preview
@Composable
private fun StartPreview() {
    CodeAppTheme {
        StartScreen(onArrowClick = {})
    }
}