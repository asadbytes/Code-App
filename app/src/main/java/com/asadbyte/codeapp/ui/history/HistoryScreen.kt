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
import androidx.compose.foundation.shape.CircleShape
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
            .padding(horizontal = 24.dp) // Slightly reduced for better balance
            .height(48.dp) // Reduced height for more elegant proportions
            .clip(RoundedCornerShape(24.dp)) // Matched to height/2 for perfect pill shape
            .background(Gray30)
            .padding(3.dp) // Reduced inner padding for tighter design
    ) {
        // Option 1: All
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(21.dp)) // Matched to outer radius minus padding
                .background(if (selectedMode == HistoryMode.All) MyYellow else Color.Transparent)
                .clickable { onModeChange(HistoryMode.All) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "All",
                color = if (selectedMode == HistoryMode.All) Color.Black else Color.White,
                fontFamily = ItimFont,
                style = MaterialTheme.typography.bodyMedium, // Slightly smaller for better proportion
                fontWeight = FontWeight.Medium // Reduced weight for cleaner look
            )
        }

        // Option 2: Favorites
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(21.dp))
                .background(if (selectedMode == HistoryMode.Favorite) MyYellow else Color.Transparent)
                .clickable { onModeChange(HistoryMode.Favorite) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Favorites",
                color = if (selectedMode == HistoryMode.Favorite) Color.Black else Color.White,
                fontFamily = ItimFont,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
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
    var selectedMode by remember { mutableStateOf(HistoryMode.All) }
    val selectedItems = remember { mutableStateListOf<HistoryItem>() }
    val isSelectionMode by remember { derivedStateOf { selectedItems.isNotEmpty() } }
    val historyItems by viewModel.history.collectAsState()
    val favoriteItems by viewModel.favorites.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray10),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar with improved spacing and positioning
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    top = 16.dp,
                    end = 24.dp,
                    bottom = 8.dp
                )
                .height(64.dp), // More reasonable height
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Better distribution
        ) {
            Text(
                text = "History",
                fontFamily = ItimFont,
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium, // Slightly smaller for better balance
                fontWeight = FontWeight.SemiBold
            )

            // Action button with consistent sizing
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp) // Standard touch target size
                    .clip(CircleShape)
                    .clickable {
                        if (isSelectionMode) {
                            viewModel.deleteItems(selectedItems.toList())
                            selectedItems.clear()
                        } else {
                            onSettingsClick()
                        }
                    }
            ) {
                if (isSelectionMode) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_history_delete),
                        contentDescription = "Delete Selected",
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = "Settings",
                        modifier = Modifier.size(24.dp),
                        tint = MyYellow
                    )
                }
            }
        }

        // Mode toggle with proper spacing
        HistoryModeToggle(
            modifier = Modifier.padding(vertical = 16.dp), // Consistent vertical spacing
            selectedMode = selectedMode,
            onModeChange = { newMode -> selectedMode = newMode }
        )

        // Content list with improved spacing
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp), // Consistent horizontal padding
            verticalArrangement = Arrangement.spacedBy(12.dp), // Slightly more space between items
            contentPadding = PaddingValues(
                top = 8.dp,
                bottom = 120.dp // Account for bottom navigation + extra space
            )
        ) {
            when (selectedMode) {
                HistoryMode.All -> {
                    items(historyItems) { item ->
                        val isSelected = selectedItems.contains(item)
                        HistoryItemComposable(
                            item = item,
                            isSelected = isSelected,
                            onItemClick = {
                                if (isSelectionMode) {
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
                                if (!isSelectionMode) {
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
                            isSelected = isSelected,
                            onItemClick = {
                                if (isSelectionMode) {
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
                                if (!isSelectionMode) {
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
    isSelected: Boolean,
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
            .clip(RoundedCornerShape(16.dp)) // Slightly smaller radius for modern look
            .background(
                if (isSelected) {
                    MyYellow.copy(alpha = 0.15f) // Reduced opacity for subtlety
                } else {
                    Gray30
                }
            )
            .fillMaxWidth()
            .padding(20.dp), // Increased padding for better touch targets
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Leading icon/indicator
        Box(
            modifier = Modifier.size(48.dp), // Consistent icon container size
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Selected",
                    tint = MyYellow,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.qr_code_scanner),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = MyYellow
                )
            }
        }

        // Content column with improved spacing
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp) // Consistent spacing between rows
        ) {
            // Title row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.content.take(25) + if (item.content.length > 25) "..." else "",
                    color = Color.White,
                    fontFamily = ItimFont,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                // Favorite button with proper touch target
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable { onToggleFavorite() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (item.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Toggle Favorite",
                        tint = if (item.isFavorite) MyYellow else Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Metadata row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.type.name,
                    color = Color.White.copy(alpha = 0.6f), // Better contrast
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = formatTimestamp(item.timestamp),
                    color = Color.White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Normal
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
