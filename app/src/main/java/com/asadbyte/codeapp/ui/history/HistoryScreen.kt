package com.asadbyte.codeapp.ui.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.data.HistoryItem
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.asadbyte.codeapp.ui.theme.Gray10
import com.asadbyte.codeapp.ui.theme.Gray30
import com.asadbyte.codeapp.ui.theme.ItimFont
import com.asadbyte.codeapp.ui.theme.MyYellow
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

// Add this enum class to your project, for example, in the same file or a dedicated model file.
enum class HistoryMode {
    All, Favorite
}

@Composable
fun HistoryModeToggle(
    modifier: Modifier = Modifier,
    selectedMode: HistoryMode,
    onModeChange: (HistoryMode) -> Unit
) {
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
                .background(if (selectedMode == HistoryMode.All) MyYellow else Color.Transparent)
                .clickable { onModeChange(HistoryMode.All) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "All",
                // Change text color for better contrast
                color = if (selectedMode == HistoryMode.All) Color.Black else Color.White,
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
                .background(if (selectedMode == HistoryMode.Favorite) MyYellow else Color.Transparent)
                .clickable { onModeChange(HistoryMode.Favorite) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Favorite",
                // Change text color for better contrast
                color = if (selectedMode == HistoryMode.Favorite) Color.Black else Color.White,
                fontFamily = ItimFont, // Your custom font
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun HistoryScreen(
    onSettingsClick: () -> Unit,
    onItemClick: (HistoryItem) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    // 1. Add state to remember the selected mode
    var selectedMode by remember { mutableStateOf(HistoryMode.All) }
    val selectedItems = remember { mutableStateListOf<HistoryItem>() }
    // Fix: Update isSelectionMode to be derived state that recomposes properly
    val isSelectionMode by remember { derivedStateOf { selectedItems.isNotEmpty() } }
    val historyItems by viewModel.history.collectAsState()
    val favoriteItems by viewModel.favorites.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray10),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Top Bar - Fixed icon visibility logic
        Row(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .padding(start = 30.dp, top = 20.dp, end = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "History",
                fontFamily = ItimFont,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.weight(1f))

            // Fix: Show delete icon only in selection mode, settings icon otherwise
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(120.dp)
            ) {
                if (isSelectionMode) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_history_delete),
                        contentDescription = "Delete Selected",
                        modifier = Modifier
                            .size(22.dp)
                            .clickable {
                                viewModel.deleteItems(selectedItems.toList())
                                selectedItems.clear()
                            }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_generate_settings),
                        contentDescription = "Settings",
                        modifier = Modifier
                            .size(120.dp)
                            .clickable { onSettingsClick() }
                    )
                }
            }
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
                .padding(horizontal = 8.dp)
                .padding(bottom = 102.dp + 10.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 18.dp)
        ) {
            when (selectedMode) {
                HistoryMode.All -> {
                    items(historyItems) { item ->
                        val isSelected = selectedItems.contains(item)
                        HistoryItemComposable(
                            item = item,
                            isSelected = isSelected, // Pass selection state
                            onItemClick = {
                                if (isSelectionMode) {
                                    // Toggle selection
                                    if (isSelected) {
                                        selectedItems.remove(item)
                                    } else {
                                        selectedItems.add(item)
                                    }
                                } else {
                                    onItemClick(item)
                                }
                            },
                            onLongClick = {
                                if (!isSelected) {
                                    selectedItems.add(item)
                                }
                            },
                            onToggleFavorite = {
                                if (!isSelectionMode) { // Only allow favorite toggle when not in selection mode
                                    viewModel.toggleFavorite(item)
                                }
                            }
                        )
                    }
                }
                HistoryMode.Favorite -> {
                    items(favoriteItems) { item ->
                        val isSelected = selectedItems.contains(item)
                        HistoryItemComposable(
                            item = item,
                            isSelected = isSelected, // Pass selection state
                            onItemClick = {
                                if (isSelectionMode) {
                                    // Toggle selection
                                    if (isSelected) {
                                        selectedItems.remove(item)
                                    } else {
                                        selectedItems.add(item)
                                    }
                                } else {
                                    onItemClick(item)
                                }
                            },
                            onLongClick = {
                                if (!isSelected) {
                                    selectedItems.add(item)
                                }
                            },
                            onToggleFavorite = {
                                if (!isSelectionMode) { // Only allow favorite toggle when not in selection mode
                                    viewModel.toggleFavorite(item)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryItemComposable(
    item: HistoryItem,
    isSelected: Boolean, // Add selection state parameter
    onItemClick: (HistoryItem) -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    onToggleFavorite: () -> Unit
) {
    Row(
        modifier = modifier
            .combinedClickable(
                onClick = { onItemClick(item) },
                onLongClick = onLongClick
            )
            .clip(RoundedCornerShape(24.dp))
            // Fix: Change background color based on selection state
            .background(
                if (isSelected) {
                    MyYellow.copy(alpha = 0.3f) // Use your app's accent color with transparency
                } else {
                    Gray30
                }
            )
            .height(100.dp)
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Add selection indicator
        if (isSelected) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Selected",
                tint = MyYellow,
                modifier = Modifier.size(45.dp)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_bottom_bar_scanner),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
        }

        Column {
            Row {
                Text(
                    text = item.content.take(10) + if (item.content.length > 10) "..." else "",
                    color = Color.White,
                    fontFamily = ItimFont,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = 120.dp)
                )

                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (item.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Toggle Favorite",
                    tint = if (item.isFavorite) MyYellow else Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onToggleFavorite() }
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
        HistoryScreen(
            onSettingsClick = {},
            onItemClick = {}
        )
    }
}

fun formatTimestamp(millis: Long): String {
    val date = Date(millis)
    val format = SimpleDateFormat("d MMM yyyy, h:mm a", Locale.getDefault())
    return format.format(date)
}
