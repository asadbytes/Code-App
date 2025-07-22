package com.asadbyte.codeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ItemType {
    SCAN, GENERATE
}

@Entity(tableName = "history_table")
data class HistoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String,
    val type: ItemType,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)