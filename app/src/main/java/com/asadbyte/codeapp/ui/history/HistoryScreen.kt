package com.asadbyte.codeapp.ui.history

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onItemClick: (HistoryItem) -> Unit
) {
    // 1. Create PagerState to control the swiping behavior (2 pages: "All" and "Favorites")
    val pagerState = rememberPagerState { 2 }
    val coroutineScope = rememberCoroutineScope()

    val historyItems by viewModel.history.collectAsState()
    val favoriteItems by viewModel.favorites.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("History") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // 2. Link the TabRow to the PagerState
            TabRow(selectedTabIndex = pagerState.currentPage) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    text = { Text("All") }
                )
                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    text = { Text("Favorites") }
                )
            }

            // 3. Use HorizontalPager to create the swipeable content area
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> HistoryList(
                        items = historyItems,
                        onItemClick = onItemClick,
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onDeleteItem = { viewModel.deleteItem(it) }
                    )
                    1 -> HistoryList(
                        items = favoriteItems,
                        onItemClick = onItemClick,
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onDeleteItem = { viewModel.deleteItem(it) }
                    )
                }
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
            Divider()
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