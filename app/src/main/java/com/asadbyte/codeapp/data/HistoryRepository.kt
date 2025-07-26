package com.asadbyte.codeapp.data

import javax.inject.Inject

class HistoryRepository @Inject constructor(private val historyDao: HistoryDao) {

    fun getAllHistory() = historyDao.getAllHistory()
    fun getFavorites() = historyDao.getFavorites()

    suspend fun insertItem(item: HistoryItem): Long {
        return historyDao.insertItem(item)
    }

    suspend fun updateItem(item: HistoryItem) {
        historyDao.updateItem(item)
    }

    suspend fun deleteItem(item: HistoryItem) {
        historyDao.deleteItem(item)
    }

    suspend fun deleteItems(items: List<HistoryItem>) {
        historyDao.deleteItems(items)
    }
}