package com.asadbyte.codeapp.di

import android.content.Context
import androidx.room.Room
import com.asadbyte.codeapp.data.AppDatabase
import com.asadbyte.codeapp.data.HistoryDao
import com.asadbyte.codeapp.data.HistoryRepository
import com.asadbyte.codeapp.presentation.generator.GeneratorViewModel
import com.asadbyte.codeapp.presentation.history.HistoryViewModel
import com.asadbyte.codeapp.presentation.scanner.ScannerViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "qr_app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideHistoryDao(database: AppDatabase): HistoryDao {
        return database.historyDao()
    }
}