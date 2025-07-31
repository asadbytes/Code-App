package com.asadbyte.codeapp.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDB623))
    ) {
        val screenHeight = maxHeight
        val screenWidth = maxWidth

        // Background images
        Image(
            painter = painterResource(R.drawable.qrcode_yellow_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Image(
            painter = painterResource(R.drawable.qrcode_homescreen_top),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
        )

        Image(
            painter = painterResource(R.drawable.qrcode_homescreen_bottom),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
        )

        // Main content with flexible layout
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                )
        ) {
            // Top spacer - flexible based on screen height
            Spacer(
                modifier = Modifier.height(
                    maxOf(screenHeight * 0.25f, 100.dp) // Minimum 100dp, or 25% of screen
                )
            )

            // QR Code icon with responsive sizing
            Image(
                painter = painterResource(R.drawable.ic_start_screen_qrcode),
                contentDescription = "QR Code Scanner",
                modifier = Modifier.size(
                    minOf(150.dp, screenWidth * 0.35f) // Max 150dp or 35% of screen width
                )
            )

            // Flexible spacer to push content to bottom
            Spacer(modifier = Modifier.weight(1f))

            // Bottom content section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 80.dp), // Space for arrow button
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.headlineLarge,
                    fontFamily = ItimFont,
                    color = Gray30,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Go and enjoy our features for free and make your life easy with us.",
                    fontFamily = ItimFont,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray20,
                    textAlign = TextAlign.Center,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            Spacer(modifier = Modifier.height(52.dp))
        }

        // Arrow button with better touch target
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_yellow_arrow),
                contentDescription = "Next Screen",
                modifier = Modifier
                    .size(80.dp)
                    .clickable { onArrowClick() }
            )
        }
    }
}

@Preview
@Composable
private fun StartPreview() {
    CodeAppTheme {
        StartScreen(onArrowClick = {})
    }
}