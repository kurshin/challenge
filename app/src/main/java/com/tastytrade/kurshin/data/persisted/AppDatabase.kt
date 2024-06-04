package com.tastytrade.kurshin.data.persisted

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tastytrade.kurshin.data.persisted.dao.QuoteDao
import com.tastytrade.kurshin.data.persisted.dao.WatchListDao
import com.tastytrade.kurshin.data.persisted.entity.QuoteEntity
import com.tastytrade.kurshin.data.persisted.entity.WatchListEntity

@Database(entities = [WatchListEntity::class, QuoteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun watchListDao(): WatchListDao
    abstract fun quoteDao(): QuoteDao
}