package com.asadbyte.codeapp.presentation.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asadbyte.codeapp.data.HistoryItem
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    historyViewModel: HistoryViewModel = hiltViewModel(),
    onItemClick: (HistoryItem) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val historyItems by historyViewModel.history.collectAsState()
    val favoriteItems by historyViewModel.favorites.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("History") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("All") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Favorites") }
                )
            }

            when (selectedTab) {
                0 -> HistoryList(
                    items = historyItems,
                    onItemClick = onItemClick,
                    onToggleFavorite = { historyViewModel.toggleFavorite(it) },
                    onDeleteItem = { historyViewModel.deleteItem(it) }
                )
                1 -> HistoryList(
                    items = favoriteItems,
                    onItemClick = onItemClick,
                    onToggleFavorite = { historyViewModel.toggleFavorite(it) },
                    onDeleteItem = { historyViewModel.deleteItem(it) }
                )
            }
        }
    }
}

@Composable
fun HistoryList(
    items: List<HistoryItem>,
    onItemClick: (HistoryItem) -> Unit,
    onToggleFavorite: (HistoryItem) -> Unit,
    onDeleteItem: (HistoryItem) -> Unit
) {
    if (items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No items yet.")
        }
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items, key = { it.id }) { item ->
            HistoryRow(item, onItemClick, onToggleFavorite, onDeleteItem)
            HorizontalDivider()
        }
    }
}

@Composable
fun HistoryRow(
    item: HistoryItem,
    onItemClick: (HistoryItem) -> Unit,
    onToggleFavorite: (HistoryItem) -> Unit,
    onDeleteItem: (HistoryItem) -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.content,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = dateFormat.format(Date(item.timestamp)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = { onToggleFavorite(item) }) {
            Icon(
                imageVector = if (item.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (item.isFavorite) MaterialTheme.colorScheme.primary else LocalContentColor.current
            )
        }
        IconButton(onClick = { onDeleteItem(item) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}