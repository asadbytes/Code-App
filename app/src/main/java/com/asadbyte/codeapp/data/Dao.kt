package com.asadbyte.codeapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: HistoryItem): Long

    @Query("SELECT * FROM history_table ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<HistoryItem>>

    @Query("SELECT * FROM history_table WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavorites(): Flow<List<HistoryItem>>

    @Query("SELECT * FROM history_table WHERE id = :id")
    suspend fun getItemById(id: String): HistoryItem?

    @Update
    suspend fun updateItem(item: HistoryItem)

    @Delete
    suspend fun deleteItem(item: HistoryItem)

    @Delete
    suspend fun deleteItems(items: List<HistoryItem>)
}