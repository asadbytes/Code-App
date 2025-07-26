package com.asadbyte.codeapp.ui.others

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.asadbyte.codeapp.ui.theme.Gray30

@Composable
fun MySplashScreen(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(Gray30)
    ) {
        Spacer(modifier = Modifier.size(220.dp))
        Image(
            painter = painterResource(R.drawable.ic_qrcode_splash),
            contentDescription = "Splash Screen"
        )
    }
}

@Preview
@Composable
private fun SplashPreview() {
    CodeAppTheme {
        MySplashScreen()
    }
}