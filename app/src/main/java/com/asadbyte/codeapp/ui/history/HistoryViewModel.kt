package com.asadbyte.codeapp.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asadbyte.codeapp.data.HistoryItem
import com.asadbyte.codeapp.data.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    val history = repository.getAllHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val favorites = repository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun toggleFavorite(item: HistoryItem) = viewModelScope.launch {
        repository.updateItem(item.copy(isFavorite = !item.isFavorite))
    }

    fun deleteItem(item: HistoryItem) = viewModelScope.launch {
        repository.deleteItem(item)
    }
}