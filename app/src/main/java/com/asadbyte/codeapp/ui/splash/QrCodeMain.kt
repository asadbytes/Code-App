package com.asadbyte.codeapp.ui.splash

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.ui.theme.CodeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCodeMain(
    onGenerateClick: () -> Unit,
    onScannerClick: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = { Text(text = "Home") })
            },
            bottomBar = {
                FixedBottomBar(
                    onGenerateClick = { onGenerateClick() },
                    onHistoryClick = { onHistoryClick() }
                )
            },
            // We leave `floatingActionButton` empty on purpose
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                content()
            }
        }

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
    val barShape = RoundedCornerShape(24.dp)
    Surface(
        shape = barShape,
        tonalElevation = 3.dp,
        border = BorderStroke(1.5.dp, Color.Yellow),
        modifier = modifier,
        color = Color(0xFF333333)
    ) {
        BottomAppBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            modifier = Modifier.height(102.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onGenerateClick() },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(64.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
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