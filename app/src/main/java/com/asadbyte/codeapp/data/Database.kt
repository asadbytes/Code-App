package com.asadbyte.codeapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [HistoryItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class) // We need this to store the Enum
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}

// Add this class to the same file or a new one to handle Enum conversion
class Converters {
    @androidx.room.TypeConverter
    fun fromItemType(value: ItemType): String = value.name

    @androidx.room.TypeConverter
    fun toItemType(value: String): ItemType = ItemType.valueOf(value)
}