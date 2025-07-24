package com.asadbyte.codeapp.ui.history

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.data.HistoryItem
import com.asadbyte.codeapp.ui.generator.generateHomeList
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.Gray30
import com.asadbyte.codeapp.ui.theme.ItimFont
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

// Add this enum class to your project, for example, in the same file or a dedicated model file.
enum class HistoryMode {
    Generated, Scanned
}

@Composable
fun HistoryModeToggle(
    modifier: Modifier = Modifier,
    selectedMode: HistoryMode,
    onModeChange: (HistoryMode) -> Unit
) {
    // Define the color for the selected state
    val yellowColor = Color(0xFFFFD600)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp) // Match the horizontal padding of your title
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp)) // Fully rounded corners
            .background(Gray30) // Background of the whole toggle bar
            .padding(4.dp) // Inner padding to create the "pill" effect
    ) {
        // Option 1: Generated
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(24.dp))
                // Apply yellow background if this mode is selected
                .background(if (selectedMode == HistoryMode.Generated) yellowColor else Color.Transparent)
                .clickable { onModeChange(HistoryMode.Generated) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Generated",
                // Change text color for better contrast
                color = if (selectedMode == HistoryMode.Generated) Color.Black else Color.White,
                fontFamily = ItimFont, // Your custom font
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Option 2: Scanned
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(24.dp))
                // Apply yellow background if this mode is selected
                .background(if (selectedMode == HistoryMode.Scanned) yellowColor else Color.Transparent)
                .clickable { onModeChange(HistoryMode.Scanned) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Scanned",
                // Change text color for better contrast
                color = if (selectedMode == HistoryMode.Scanned) Color.Black else Color.White,
                fontFamily = ItimFont, // Your custom font
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun HistoryHomeNew(
    onSettingsClick: () -> Unit,
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    // 1. Add state to remember the selected mode
    var selectedMode by remember { mutableStateOf(HistoryMode.Generated) }
    val historyItems by viewModel.history.collectAsState()
    val favoriteItems by viewModel.favorites.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray10),
        horizontalAlignment = Alignment.CenterHorizontally // Center children like the toggle
    ) {

        // Top Bar (No changes)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "History",
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

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Add the new toggle switch
        HistoryModeToggle(
            selectedMode = selectedMode,
            onModeChange = { newMode -> selectedMode = newMode }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 3. Update LazyColumn to show content based on the selected mode
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 18.dp)
        ) {
            when (selectedMode) {
                HistoryMode.Generated -> {
                    items(historyItems) {
                        HistoryItemComposable(it)
                    }
                }
                HistoryMode.Scanned -> {
                    // Your UI for "Scanned" history items
                    // For example, you can show different items or an empty state
                    items(favoriteItems) {
                        HistoryItemComposable(it) // Using the same for demonstration
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItemComposable(
    item: HistoryItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Gray30)
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_bottom_bar_scanner),
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )
        Column {
            Row {
                Text(
                    text = item.content,
                    color = Color.White,
                    fontFamily = ItimFont,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.ic_history_delete),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
            Row {
                Text(
                    text = item.type.name,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = formatTimestamp(item.timestamp),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview
@Composable
private fun HistoryItemPreview() {
    CodeAppTheme {
        HistoryHomeNew(
            onSettingsClick = {},
            navController = NavController(LocalContext.current)
        )
    }
}

fun formatTimestamp(millis: Long): String {
    val date = Date(millis)
    val format = SimpleDateFormat("d MMM yyyy, h:mm a", Locale.getDefault())
    return format.format(date)
}
