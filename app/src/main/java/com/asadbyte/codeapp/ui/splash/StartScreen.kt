package com.asadbyte.codeapp.ui.splash

import android.widget.ImageButton
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.asadbyte.codeapp.ui.theme.Gray20
import com.asadbyte.codeapp.ui.theme.Gray30

@Composable
fun StartScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.start_screen_bg),
            contentDescription = null,
            modifier = modifier.fillMaxSize()
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
                    color = Gray30
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "Go and enjoy our features for free and make your life easy with us.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray20,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.ic_yellow_arrow),
            contentDescription = "Next Screen", // Good for accessibility
            modifier = Modifier
                .align(Alignment.BottomEnd) // Positions the image in the corner
                .padding(horizontal = 45.dp, vertical = 70.dp) // Adds spacing from the edges
                .size(100.dp) // Give it a defined size
                .clickable { /* Handle button click */ }
        )
    }
}


@Preview
@Composable
private fun StartPreview() {
    CodeAppTheme {
        StartScreen()
    }
}