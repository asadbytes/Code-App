package com.asadbyte.codeapp.ui.splash

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.Gray30

@Composable
fun QrCodeMain(
    onGenerateClick: () -> Unit,
    onScannerClick: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                content()
            }
        }

        FixedBottomBar(
            onGenerateClick = onGenerateClick,
            onHistoryClick = onHistoryClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                /*.padding(bottom = 16.dp)*/
        )

        // ✅ FAB OVERLAPPING BOTTOM BAR
        FloatingActionButton(
            onClick = { onScannerClick() },
            containerColor = Color.Transparent,
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-38).dp) // Negative offset to raise half of 102dp bar height
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_bottom_bar_scanner),
                contentDescription = "Scanner",
                modifier = Modifier.size(120.dp) // Adjust to your desired FAB size
            )
        }
    }
}

@Composable
fun FixedBottomBar(
    onGenerateClick: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 6.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Gray30)
            .border(
                width = 1.5.dp,
                color = Color.Yellow,
                shape = RoundedCornerShape(24.dp)
            )
            .height(102.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize() // Changed from fillMaxWidth() to fillMaxSize()
        ) {
            IconButton(
                onClick = { onGenerateClick() },
                modifier = Modifier
                    .padding(16.dp)
                    .size(64.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bottom_bar_generate),
                        contentDescription = "Generate",
                        tint = Color.White
                    )
                    Text(
                        text = "Generate",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            IconButton(
                onClick = { onHistoryClick() },
                modifier = Modifier
                    .padding(16.dp)
                    .size(64.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bottom_bar_history),
                        contentDescription = "History",
                        tint = Color.White
                    )
                    Text(
                        text = "History",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    CodeAppTheme {
        QrCodeMain(
            onGenerateClick = {},
            onScannerClick = {},
            onHistoryClick = {},
            content = { Text(text = "Content") }
        )
    }
}